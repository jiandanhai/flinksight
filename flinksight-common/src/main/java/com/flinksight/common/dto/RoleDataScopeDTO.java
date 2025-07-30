package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色-数据权限范围关联DTO")
public class RoleDataScopeDTO {
    private Long id;
    private Long roleId;
    private Long dataScopeId;
    private Integer isDeleted;
}
