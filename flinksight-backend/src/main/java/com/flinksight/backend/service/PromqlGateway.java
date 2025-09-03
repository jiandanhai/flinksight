package com.flinksight.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

/**
 * Prometheus 网关：将常用聚合（健康/容量/利用率）封装为后端 API，避免前端直接拼复杂 PromQL。
 */
@Service
@RequiredArgsConstructor
public class PromqlGateway {
    @Value("${prometheus.baseUrl}")
    private String prom;
    private final RestTemplate rt = new RestTemplate();

    public String query(String promql) {
        URI uri = URI.create(prom + "/api/v1/query?query=" + promql);
        return rt.getForObject(uri, String.class);
    }

    public Map<String, Object> healthScore(String ns) {
        // 示例：slots 使用率 + checkpoint 失败率 等聚合（返回 JSON）
        String slots = query("1 - (flink_jobmanager_slots_available / flink_jobmanager_slots_total)");
        return Map.of("namespace", ns, "slotsUsagePromQL", slots);
    }
}