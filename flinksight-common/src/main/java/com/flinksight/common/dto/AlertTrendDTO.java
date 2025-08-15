package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class AlertTrendDTO  implements Serializable {
    @Schema(description = "时间列表")
    private List<String> times;

    @Schema(description = "总告警数量")
    private List<Long> total;

    @Schema(description = "严重告警数量")
    private List<Long> fatal;

    @Schema(description = "预警告警数量")
    private List<Long> warn;
}