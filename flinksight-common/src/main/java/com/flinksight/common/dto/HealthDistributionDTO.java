package com.flinksight.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * 集群健康状态分布统计DTO
 * 用于首页卡片，统计健康/警告/异常集群数量
 * 非数据库持久化，仅作为接口输出对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "集群健康分布统计DTO")
public class HealthDistributionDTO implements Serializable {

    @Schema(description = "健康集群数量")
    private Integer healthyCount;

    @Schema(description = "警告集群数量")
    private Integer warningCount;

    @Schema(description = "异常集群数量")
    private Integer errorCount;

    @Schema(description = "统计时间（yyyy-MM-dd HH:mm:ss）")
    private String statTime;
}
