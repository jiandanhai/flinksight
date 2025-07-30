package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 第三方集成配置DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "第三方集成配置DTO")
public class IntegrationConfigDTO {
    private Long id;
    private String name;
    private String type;
    private String configJson;
    private Long tenantId;
    private Integer status;
    private LocalDateTime createTime;
    private Integer isDeleted;
}
