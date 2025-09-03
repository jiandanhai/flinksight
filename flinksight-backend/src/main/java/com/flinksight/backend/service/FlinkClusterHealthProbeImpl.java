// src/main/java/com/flinksight/backend/service/cluster/probe/FlinkClusterHealthProbe.java
package com.flinksight.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.dto.ClusterHealthDTO;
import com.flinksight.common.service.cluster.probe.ClusterHealthProbe;
import com.flinksight.common.utils.Jsons;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class FlinkClusterHealthProbeImpl implements ClusterHealthProbe {

  private final OkHttpClient http = new OkHttpClient.Builder()
      .connectTimeout(2, TimeUnit.SECONDS)
      .readTimeout(4, TimeUnit.SECONDS)
      .callTimeout(6, TimeUnit.SECONDS)
      .build();

  @Override public boolean supports(String type) {
    return "FLINK".equalsIgnoreCase(type);
  }

  @Override
  public ClusterHealthDTO check(ClusterDTO c) throws Exception {
    long start = System.currentTimeMillis();
    String base = trimSlash(c.getEndpoint());

    // 1) /overview
    JsonNode overview = getJson(base + "/overview");
    // 2) /jobs/overview
    JsonNode jobs = getJson(base + "/jobs/overview").path("jobs");
    // 3) /taskmanagers
    JsonNode tms = getJson(base + "/taskmanagers").path("taskmanagers");

    int jobsRunning = countByState(jobs, "RUNNING");
    int jobsFailed  = countByState(jobs, "FAILED");

    Map<String, Object> samples = new HashMap<>();
    samples.put("flink.version", overview.path("flink-version").asText(null));
    samples.put("flink.commit", overview.path("flink-commit").asText(null));
    samples.put("jobs.total", jobs.isArray() ? jobs.size() : 0);
    samples.put("jobs.running", jobsRunning);
    samples.put("jobs.failed", jobsFailed);
    samples.put("taskmanagers", tms.isArray() ? tms.size() : 0);

    ClusterHealthDTO.Status status =
        (jobsFailed > 0) ? ClusterHealthDTO.Status.DEGRADED : ClusterHealthDTO.Status.UP;

    return ClusterHealthDTO.builder()
        .name(c.getName())
        .type(c.getType())
        .endpoint(c.getEndpoint())
        .status(status)
        .latencyMs(System.currentTimeMillis() - start)
        .message("Flink REST OK")
        .samples(samples)
        .checkedAt(Instant.now())
        .build();
  }

  private JsonNode getJson(String url) throws Exception {
    Request req = new Request.Builder()
            .url(url)
            .get()
            .header("Accept", "application/json")
            .build();

    try (Response resp = http.newCall(req).execute()) {
      if (!resp.isSuccessful()) {
        throw new RuntimeException("HTTP " + resp.code() + " : " + url);
      }

      try (ResponseBody body = resp.body()) {
        if (body == null) {
          throw new RuntimeException("Empty body : " + url);
        }

        // 推断字符集，默认 UTF-8（JSON 标准默认也是 UTF-8）
        Charset cs = StandardCharsets.UTF_8;
        MediaType ct = body.contentType();
        if (ct != null) {
          Charset detected = ct.charset(StandardCharsets.UTF_8);
          if (detected != null) cs = detected;
        }

        // 一次性读取字节并按字符集解码
        String text = new String(body.bytes(), cs);
        return  Jsons.readTree(text);
      }
    }
  }

  private static int countByState(JsonNode jobs, String state) {
    if (jobs == null || !jobs.isArray()) return 0;
    int n = 0;
    for (JsonNode j : jobs) if (state.equalsIgnoreCase(j.path("state").asText())) n++;
    return n;
  }

  private static String trimSlash(String s) {
    if (s == null) return "";
    return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
  }
}
