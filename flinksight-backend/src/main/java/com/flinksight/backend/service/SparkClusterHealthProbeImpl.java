// src/main/java/com/flinksight/backend/service/cluster/probe/SparkClusterHealthProbe.java
package com.flinksight.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.dto.ClusterHealthDTO;
import com.flinksight.common.service.cluster.probe.ClusterHealthProbe;
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
public class SparkClusterHealthProbeImpl implements ClusterHealthProbe {

  private final OkHttpClient http = new OkHttpClient.Builder()
      .connectTimeout(2, TimeUnit.SECONDS)
      .readTimeout(4, TimeUnit.SECONDS)
      .callTimeout(6, TimeUnit.SECONDS)
      .build();

  @Override public boolean supports(String type) {
    return "SPARK".equalsIgnoreCase(type);
  }

  @Override
  public ClusterHealthDTO check(ClusterDTO c) throws Exception {
    long start = System.currentTimeMillis();
    String base = trimSlash(c.getEndpoint());

    // 1) 尝试 History Server /api/v1/version
    JsonNode version = getJson(base + "/api/v1/version");
    // 2) 活动应用 /api/v1/applications?status=running
    JsonNode apps = getJson(base + "/api/v1/applications?status=running");

    int appsRunning = apps.isArray() ? apps.size() : 0;

    Map<String, Object> samples = new HashMap<>();
    samples.put("spark.version", version.asText(null));
    samples.put("apps.running", appsRunning);

    ClusterHealthDTO.Status status =
        appsRunning >= 0 ? ClusterHealthDTO.Status.UP : ClusterHealthDTO.Status.UNKNOWN;

    return ClusterHealthDTO.builder()
        .name(c.getName())
        .type(c.getType())
        .endpoint(c.getEndpoint())
        .status(status)
        .latencyMs(System.currentTimeMillis() - start)
        .message("Spark History REST OK")
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
        return new ObjectMapper().readTree(text);
      }
    }
  }

  private static String trimSlash(String s) {
    if (s == null) return "";
    return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
  }
}
