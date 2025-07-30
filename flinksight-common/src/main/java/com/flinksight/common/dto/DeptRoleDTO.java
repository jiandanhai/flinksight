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
@Schema(description = "部门-角色关联DTO")
public class DeptRoleDTO {
    private Long id;
    private Long deptId;
    private Long roleId;
    private Integer isDeleted;
}
