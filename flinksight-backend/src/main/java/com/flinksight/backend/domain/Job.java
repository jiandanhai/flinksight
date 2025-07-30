package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务实体
 * Job Entity
 */
@Data
@Entity
@Table(name = "job")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "任务表")
@Where(clause = "is_deleted=0")
public class Job implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "任务ID")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "所属租户ID")
    private Long tenantId;

    @Column(name = "cluster_id", nullable = false)
    @Schema(description = "所属集群ID")
    private Long clusterId;

    @Column(nullable = false, length = 64)
    @Schema(description = "任务名")
    private String name;

    @Column(length = 32)
    @Schema(description = "类型(streaming/batch)")
    private String type;

    @Column(length = 16)
    @Schema(description = "状态(运行/异常/已停止等)")
    private String status;

    @Column(name = "owner_id")
    @Schema(description = "负责人ID")
    private Long ownerId;

    @Column(name = "start_time")
    @Schema(description = "启动时间")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    @Schema(description = "软删除")
    private Integer isDeleted;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
