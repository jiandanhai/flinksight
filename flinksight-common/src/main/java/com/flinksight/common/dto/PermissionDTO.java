package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO implements Serializable {
    private Long id;
    private String code;
    private String name;
    private String desc;
    private String type; // MENU/BUTTON/API
    @Schema(description = "启停标记 0=禁用 1=启用")
    private Integer enabled;
    @Schema(description = "软删除标记 0=正常 1=删除")
    private Integer isDeleted;
}
