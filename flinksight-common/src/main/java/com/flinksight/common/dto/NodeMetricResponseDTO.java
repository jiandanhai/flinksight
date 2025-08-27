package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "（集群）节点指标返回")
public class NodeMetricResponseDTO {
    @Schema(description = "时间轴（ISO-8601）")
    private List<String> times;
    @Schema(description = "CPU 使用率（%）")
    private List<Double> cpu;
    @Schema(description = "内存使用率（%）")
    private List<Double> memory;
    @Schema(description = "活跃节点数")
    private List<Integer> activeNodes;
}