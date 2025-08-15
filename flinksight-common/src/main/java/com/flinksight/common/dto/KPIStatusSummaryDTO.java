package com.flinksight.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * 大盘核心指标统计DTO
 * 用于首页、可视化大屏，展示集群数、任务数、报警数、健康度等KPI
 * 非数据库字段，仅接口聚合用
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "大盘核心指标统计DTO")
public class KPIStatusSummaryDTO implements Serializable {

    @Schema(description = "集群总数")
        private Integer clusterCount;

    @Schema(description = "任务总数")
    private Integer jobCount;

    @Schema(description = "报警事件数")
    private Integer alertCount;

    @Schema(description = "活跃任务数")
    private Integer activeJobCount;

    @Schema(description = "整体健康得分（0-100）")
    private Integer healthScore;

    @Schema(description = "失败作业数量")
    private long failedJobs;

    @Schema(description = "用户数量")
    private long userCount;

    @Schema(description = "统计时间（yyyy-MM-dd HH:mm:ss）")
    private String statTime;
}
