package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 作业-用户-权限关系DTO
 * 用于API返回、权限管理前端、批量导入/导出等
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "作业权限分配DTO")
public class JobPermissionDTO  implements Serializable {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "作业ID")
    private Long jobId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "用户ID")
    private String userId;

    // 业务场景可选：冗余权限名、作业名、授权人等
    @Schema(description = "权限编码（可选）")
    private String permissionCode;

    @Schema(description = "权限名称（可选）")
    private String permissionName;

    @Schema(description = "作业名称（可选）")
    private String jobName;
}
