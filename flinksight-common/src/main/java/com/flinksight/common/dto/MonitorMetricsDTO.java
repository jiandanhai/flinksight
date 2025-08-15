package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data @NoArgsConstructor @AllArgsConstructor
public class MonitorMetricsDTO  implements Serializable {
    @Schema(description = "当前告警总数")
    private long alertCount;

    @Schema(description = "运行中的作业数量")
    private long jobRunning;

    @Schema(description = "健康集群数量")
    private long clusterHealthy;

    @Schema(description = "用户总数")
    private long userCount;
}