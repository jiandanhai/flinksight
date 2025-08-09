package com.flinksight.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * 业务转化漏斗分析DTO
 * 用于前端业务漏斗分析图，统计各阶段任务数量和转化情况
 * 非数据库持久化，仅作为接口输出对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "业务转化漏斗统计DTO")
public class JobFunnelDTO implements Serializable {

    @Schema(description = "业务阶段，如NEW、RUNNING、COMPLETED、FAILED等")
    private String stage;

    @Schema(description = "当前阶段任务数量")
    private Integer count;

    @Schema(description = "上一阶段转化率（百分比字符串，如87.2%）")
    private String conversionRate;

    @Schema(description = "阶段业务描述")
    private String stageDesc;
}
