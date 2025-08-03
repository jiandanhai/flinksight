package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统设置DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统设置DTO")
public class SystemSettingsDTO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "设置项名称")
    private String name;

    @Schema(description = "设置项编码（唯一）")
    private String code;

    @Schema(description = "设置值")
    private String value;

    @Schema(description = "设置项描述")
    private String description;

    @Schema(description = "所属租户ID")
    private Long tenantId;

    @Schema(description = "软删除 0正常 1删除")
    private Integer isDeleted;
}
