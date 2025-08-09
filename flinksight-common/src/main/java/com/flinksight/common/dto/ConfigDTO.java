package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统配置DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统配置DTO")
public class ConfigDTO  implements Serializable {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "配置项名称")
    private String name;

    @Schema(description = "配置项编码（唯一）")
    private String code;

    @Schema(description = "配置项的值")
    private String value;

    @Schema(description = "配置项描述")
    private String description;

    @Schema(description = "所属租户ID")
    private Long tenantId;

    @Schema(description = "配置项分组")
    private String group;

    @Schema(description = "是否启用 1启用 0禁用")
    private Integer enabled;

    @Schema(description = "软删除 0正常 1删除")
    private Integer isDeleted;
}
