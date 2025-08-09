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
@Schema(description = "用户-租户关联DTO")
public class UserTenantDTO  implements Serializable {
    private Long id;
    private Long userId;
    private Long tenantId;
    private Integer isDeleted;
}
