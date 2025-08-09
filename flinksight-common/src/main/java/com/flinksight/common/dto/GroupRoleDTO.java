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
@Schema(description = "组织-角色关联DTO")
public class GroupRoleDTO  implements Serializable {
    private Long id;
    private Long groupId;
    private Long roleId;
    private Integer isDeleted;
}
