package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "用户-角色关联DTO")
public class UserRoleDTO implements Serializable {
    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "角色ID")
    private Long roleId;
    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "分配时间")
    private LocalDateTime assignTime;
    private Integer isDeleted;
}
