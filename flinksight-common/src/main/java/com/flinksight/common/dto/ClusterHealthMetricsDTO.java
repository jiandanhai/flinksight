package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "集群健康指标统计 DTO")
public class ClusterHealthMetricsDTO implements Serializable {

    @Schema(description = "总集群数量")
    private int totalClusters;

    @Schema(description = "活跃节点总数")
    private int totalActiveNodes;

    @Schema(description = "平均 CPU 使用率")
    private double avgCpuUsage;

    @Schema(description = "平均内存使用率")
    private double avgMemoryUsage;

    @Schema(description = "统计时间")
    private String statTime;
}
