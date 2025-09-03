package com.flinksight.common.model;

import lombok.*;

import java.util.*;

/**
 * 统一 Cluster 规范：对 Flink/Spark 集群进行注册/健康/容量/操作（扩缩容、滚更、重启）。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusterSpec {
    public enum Engine {FLINK, SPARK}

    public enum Type {k8s, yarn, standalone}

    private Engine engine;
    private Type type;
    private String namespace;
    private String displayName;
    private String version;
    private Operator operator;
    private Endpoints endpoints;
    private Metrics metrics;
    private Capacity capacity;
    private Map<String, String> labels;

    @Data
    public static class Operator {
        boolean installed;
        String name;
    }

    @Data
    public static class Endpoints {
        String flinkRest;
        String sparkHistory;
        String k8sApi;
    }

    @Data
    public static class Metrics {
        String prometheus;
        String scrape;
    }

    @Data
    public static class Capacity {
        Integer slots;
        Double cpu;
        String memory;
    }
}