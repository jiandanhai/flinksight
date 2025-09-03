package com.flinksight.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flinksight.backend.domain.Cluster;
import com.flinksight.backend.repository.ClusterRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.FlinkRestartFromLastRequestDTO;
import com.flinksight.common.dto.FlinkSavepointRequestDTO;
import com.flinksight.common.dto.FlinkSavepointResponseDTO;
import com.flinksight.common.dto.JobActionAckDTO;
import com.flinksight.common.service.FlinkJobOpsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

/** Flink 自愈动作：Savepoint 与从最近 Savepoint 重启（任务化） */
@Service @RequiredArgsConstructor
public class FlinkJobOpsServiceImpl implements FlinkJobOpsService {

  private final ClusterRepository clusters;
  private final ObjectMapper om;
  private final OkHttpClient http = new OkHttpClient();

  @Transactional
  @Override public FlinkSavepointResponseDTO triggerSavepoint(FlinkSavepointRequestDTO req) {

    Cluster c = clusters.search(SecurityUtil.getCurrentTenantId(), "FLINK", null, req.getClusterName(), PageRequest.of(0,1))
        .stream().findFirst()
        .orElseGet(() -> clusters.findByTenantIdAndName(SecurityUtil.getCurrentTenantId(), req.getClusterName()).orElse(null));
    if (c == null) throw new ResponseStatusException(NOT_FOUND, "未找到 Flink 集群：" + req.getClusterName());

    String base = trim(c.getEndpoint());
    try {
      // 1) POST /jobs/{jid}/savepoints
      String url = base + "/jobs/" + req.getJobIdHex() + "/savepoints";
      String body = req.getTargetDirectory()==null? "{\"triggerId\":null}" :
          "{\"target-directory\":\""+ req.getTargetDirectory() +"\"}";
      Request r = new Request.Builder().url(url).post(RequestBody.create(body, MediaType.parse("application/json")))
          .header("Accept","application/json").build();
      String triggerId;
      try (Response resp = http.newCall(r).execute()) {
        if (!resp.isSuccessful()) throw new RuntimeException("HTTP "+resp.code());
        JsonNode node = om.readTree(resp.body().string());
        triggerId = Optional.ofNullable(node.path("request-id").asText(null))
            .orElse(node.path("triggerId").asText(null)); // 兼容不同版本字段
      }
      if (triggerId==null) throw new RuntimeException("未返回 triggerId");

      // 2) 轮询 operation 直到完成
      String op = base + "/jobs/" + req.getJobIdHex() + "/savepoints/" + triggerId;
      String location = pollSavepointLocation(op, Duration.ofSeconds(60));
      return FlinkSavepointResponseDTO.builder().accepted(true).location(location).message("OK").build();
    } catch (Exception e) {
      throw new ResponseStatusException(BAD_REQUEST, "触发 savepoint 失败: " + e.getMessage());
    }
  }

  @Override public JobActionAckDTO restartFromLast(FlinkRestartFromLastRequestDTO req) {

    // 这里**不直接**调用 Flink 提交 jar（保持监控域与部署域职责分离）
    // 而是创建一个平台“任务”（交给你们现有的部署/运维流水线去执行：从最近 savepoint 重启，可选并行度）
    // 为了自包含，这里直接返回一个假的 taskId；你在你们的 Task 模块里把这一步接上。
    String taskId = "TASK-" + System.currentTimeMillis();
    return JobActionAckDTO.builder().accepted(true)
        .taskId(taskId)
        .message("请求已受理：将从最近 savepoint 重启作业 " + req.getJobName() +
                 (req.getNewParallelism()!=null? "，并行度="+req.getNewParallelism():""))
        .build();
  }

  // ---------- helpers ----------
  private String pollSavepointLocation(String opUrl, Duration timeout) throws Exception {
    long deadline = System.currentTimeMillis() + timeout.toMillis();
    while (System.currentTimeMillis() < deadline) {
      Request q = new Request.Builder().url(opUrl).get().header("Accept","application/json").build();
      try (Response resp = http.newCall(q).execute()) {
        if (!resp.isSuccessful()) throw new RuntimeException("HTTP "+resp.code());
        JsonNode n = om.readTree(resp.body().string());
        // 不同版本字段名差异：'operation'/'status'/'location'
        if (n.path("status").path("id").asText("").equalsIgnoreCase("COMPLETED")
            || n.path("operation").path("status").asText("").equalsIgnoreCase("COMPLETED")) {
          String loc = Optional.ofNullable(n.path("operation").path("location").asText(null))
              .orElse(n.path("location").asText(null));
          if (loc==null) throw new RuntimeException("完成但未返回 location");
          return loc;
        }
      }
      Thread.sleep(1500L);
    }
    throw new RuntimeException("轮询超时");
  }
  private static String trim(String s){ return s!=null && s.endsWith("/")? s.substring(0,s.length()-1): s; }
  private Long requireTenant(){ Long t = SecurityUtil.getCurrentTenantId(); if (t==null) throw new ResponseStatusException(UNAUTHORIZED, "未识别当前租户"); return t; }
}