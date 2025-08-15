package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 集群状态趋势 DTO（用于前端折线图）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "集群状态趋势 DTO")
public class ClusterTrendDTO implements Serializable {

    @Schema(description = "时间序列")
    private List<String> times;

    @Schema(description = "健康数量")
    private List<Integer> healthy;

    @Schema(description = "预警数量")
    private List<Integer> warning;

    @Schema(description = "异常数量")
    private List<Integer> critical;


}
