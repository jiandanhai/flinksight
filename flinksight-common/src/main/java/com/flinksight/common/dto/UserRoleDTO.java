package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户-角色关联DTO")
public class UserRoleDTO {
    private Long id;
    private Long userId;
    private Long roleId;
    private Long tenantId;
    private LocalDateTime assignTime;
    private Integer isDeleted;
}
