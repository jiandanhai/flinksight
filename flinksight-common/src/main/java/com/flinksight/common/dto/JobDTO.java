package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO implements Serializable {
    @Schema(description = "任务ID")
    private Long id;

    @Schema(description = "所属租户ID")
    private Long tenantId;

    @Schema(description = "所属集群ID，可为空")
    private Long clusterId;

    @Schema(description = "任务名")
    private String name;

    @Schema(description = "类型 (streaming/batch)")
    private String type;

    @Schema(description = "计算引擎 (FLINK/SPARK)")
    private String engine;

    @Schema(description = "环境 (dev/staging/prod)")
    private String env;

    @Schema(description = "状态：0=CREATED,1=RUNNING,2=FAILED,3=STOPPED,4=SUCCESS")
    private Short status;

    @Schema(description = "负责人ID")
    private Long ownerId;

    @Schema(description = "启动时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "JobSpec 快照（JSONB）")
    private String spec;

    @Schema(description = "软删除；0=未删 1=已删")
    private Integer isDeleted = 0;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
