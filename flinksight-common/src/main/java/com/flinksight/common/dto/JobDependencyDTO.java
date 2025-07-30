package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 作业依赖DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "作业依赖DTO")
public class JobDependencyDTO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "作业ID")
    private Long jobId;

    @Schema(description = "依赖名称")
    private String dependencyName;

    @Schema(description = "依赖类型，如JAR、Python包、其他")
    private String dependencyType;

    @Schema(description = "依赖文件存储路径/URL")
    private String dependencyPath;

    @Schema(description = "依赖描述")
    private String description;

    @Schema(description = "所属租户ID")
    private Long tenantId;

    @Schema(description = "软删除 0正常 1删除")
    private Integer isDeleted;
}
