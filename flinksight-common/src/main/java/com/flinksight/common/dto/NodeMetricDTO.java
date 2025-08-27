package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/** 从 ClusterStatusHistory 取汇总/最新监控值 */
@Data
@Schema(description = "集群监控指标 DTO（来自最近一次采集）")
public class NodeMetricDTO {
    private Integer activeNodeCount;
    private Double cpuUsage;
    private Double memoryUsage;
    private String queueLoadJson;
    private LocalDateTime collectTime;
}