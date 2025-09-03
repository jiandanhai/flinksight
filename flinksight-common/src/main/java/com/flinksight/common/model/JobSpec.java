package com.flinksight.common.model;

import lombok.*;

import java.util.*;

/**
 * 统一 Job 规范：控制面与运行面的契约。后端将其渲染为 K8s CRD 或 Runner 提交参数。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSpec {
    public enum Engine {FLINK, SPARK}

    private Engine engine;                 // 计算引擎
    private Long tenantId;                 // 租户 ID
    private String env;                    // 环境 dev/staging/prod
    private ClusterRef clusterRef;         // 运行集群引用
    private Artifact artifact;             // 制品/入口
    private Resources resources;           // 资源规格
    private Checkpoint checkpoint;         // Checkpoint 配置（Flink）
    private Upgrade upgrade;               // 升级策略（Flink: savepoint/stateless）
    private Restart restart;               // 重启策略
    private MetricsSink metricsSink;       // 指标/事件下沉（Kafka/Prom/ES）
    private Integer alertPolicyId;         // 告警策略引用
    private Map<String, String> labels;     // 标签
    private List<String> secrets;          // 运行所需 Secret

    @Data
    public static class ClusterRef {
        String type;
        String namespace;
        String queue;
        Map<String, String> labels;
    }

    @Data
    public static class Artifact {
        String image;
        String jar;
        String mainClass;
        List<String> args;
    }

    @Data
    public static class Resources {
        Integer parallelism;
        FlinkRes flink;
        SparkRes spark;
    }

    @Data
    public static class FlinkRes {
        Integer taskSlots;
        String jmMemory;
        String tmMemory;
    }

    @Data
    public static class SparkRes {
        Integer executorCores;
        String executorMemory;
        Integer instances;
    }

    @Data
    public static class Checkpoint {
        String interval;
        String path;
        String mode;
    }

    @Data
    public static class Upgrade {
        String mode;
        String strategy;
    }

    @Data
    public static class Restart {
        String type;
        Integer attempts;
        String delay;
    }

    @Data
    public static class MetricsSink {
        String type;
        Map<String, String> param;
    }
}