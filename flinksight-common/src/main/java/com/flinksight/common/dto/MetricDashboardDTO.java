package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 指标看板DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "指标看板DTO")
public class MetricDashboardDTO  implements Serializable {
    private Long id;
    private String name;
    private String configJson;
    private Long tenantId;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
}
