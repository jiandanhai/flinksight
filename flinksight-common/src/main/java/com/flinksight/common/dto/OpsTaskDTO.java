package com.flinksight.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 运维自动化任务DTO
 * 用于运维任务新建、展示、管理
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "运维自动化任务DTO")
public class OpsTaskDTO implements Serializable {

    @Schema(description = "任务ID")
    private Long id;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "自动化运维模板ID")
    private Long templateId;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "任务类型（如备份、扩容、升级等）")
    private String type;

    @Schema(description = "状态（PENDING、RUNNING、SUCCESS、FAILED等）")
    private String status;

    @Schema(description = "任务描述")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "执行时间")
    private LocalDateTime executedAt;

    @Schema(description = "是否删除 0=正常 1=删除")
    private Integer isDeleted;
}
