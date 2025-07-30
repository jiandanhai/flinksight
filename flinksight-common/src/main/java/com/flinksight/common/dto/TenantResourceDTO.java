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
@Schema(description = "租户-资源关联DTO")
public class TenantResourceDTO {
    private Long id;
    private Long tenantId;
    private Long resourceId;
    private Integer isDeleted;
}
