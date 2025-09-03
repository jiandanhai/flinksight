package com.flinksight.flinkjob.collector;

import org.apache.flink.api.connector.source.SourceSplit;

import java.io.Serializable;

/**
 * Split / 枚举器 / 状态 / 序列化器（精简稳定实现）
 */
public final class FlinkRestSplit implements SourceSplit, Serializable {
    private final String id;
    public final String restUrl;
    public final long tenantId;
    public final String env;
    public final String cluster;
    public final String clusterId;
    public final String clusterType;

    public FlinkRestSplit(String id, String restUrl, long tenantId, String env,
                          String cluster, String clusterId, String clusterType) {
        this.id = id;
        this.restUrl = restUrl;
        this.tenantId = tenantId;
        this.env = env;
        this.cluster = cluster;
        this.clusterId = clusterId;
        this.clusterType = clusterType;
    }
    @Override public String splitId() { return id; }
}
