package com.flinksight.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.service.ClusterDiscoveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 集群自动发现与元数据采集服务实现
 * 负责探测YARN、K8S、Standalone等多环境集群的可用性与API信息
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClusterDiscoveryServiceImpl implements ClusterDiscoveryService {

    //使用 Spring 管理的 ObjectMapper（会带上你全局的 JavaTimeModule 等配置）
    private final ObjectMapper mapper;
    /**
     * 批量自动发现集群状态
     */
    @Override
    public List<ClusterDTO> discoverClusters(List<ClusterDTO> configs) {
        List<ClusterDTO> result = new ArrayList<>();
        if (configs != null) {
            for (ClusterDTO config : configs) {
                result.add(discoverCluster(config));
            }
        }
        return result;
    }

    /**
     * 探测单个集群的状态与元信息
     */
    @Override
    public ClusterDTO discoverCluster(ClusterDTO config) {
        try {
            if ("YARN".equalsIgnoreCase(config.getType())) {
                // 采集YARN ResourceManager API
                String yarnApi = config.getApiEndpoint() + "/ws/v1/cluster/info";
                String resp = Request.Get(yarnApi).connectTimeout(2000).execute().returnContent().asString();
                JsonNode info = mapper.readTree(resp);
                config.setStatus(
                    "STARTED".equalsIgnoreCase(info.path("clusterInfo").path("state").asText("UNKNOWN")) ? 1 : 0
                );
                // 可补充其它采集字段
            } else if ("K8S".equalsIgnoreCase(config.getType())) {
                // 可集成Kubernetes Java SDK/Rest API探测（简化为假定API联通为在线）
                config.setStatus(1);
            } else if ("Standalone".equalsIgnoreCase(config.getType())) {
                // Flink Standalone REST接口
                String resp = Request.Get(config.getApiEndpoint() + "/overview").connectTimeout(2000)
                        .execute().returnContent().asString();
                JsonNode info = mapper.readTree(resp);
                config.setStatus(info.path("taskmanagers").isArray() ? 1 : 0);
            } else {
                config.setStatus(0); // 未知类型视为离线
            }
        } catch (Exception e) {
            log.warn("集群自动发现失败: {}", config.getApiEndpoint(), e);
            config.setStatus(0);
        }
        return config;
    }
}
