package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "角色DTO")
public class RoleDTO implements Serializable {
    @Schema(description = "角色ID")
    private Long id;
    @Schema(description = "角色编码")
    private String code;
    @Schema(description = "角色名称")
    private String name;
    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "角色备注")
    private String remark;
    @Schema(description = "是否删除 0=正常 1=删除")
    private Integer isDeleted;

    public RoleDTO(Long id,String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
