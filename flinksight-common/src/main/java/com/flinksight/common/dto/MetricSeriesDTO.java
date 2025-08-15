package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
    public class MetricSeriesDTO  implements Serializable {
    @Schema(description = "时间序列")
    private List<String> times;

    @Schema(description = "指标值")
    private List<Double> values;
    }