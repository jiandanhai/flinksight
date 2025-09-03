package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "job",
        uniqueConstraints = @UniqueConstraint(name = "uk_job_name_cluster_tenant", columnNames = {"job_name", "cluster_id","tenant_id"}),
        indexes = {
                @Index(name = "idx_tenant", columnList = "tenant_id"),
                @Index(name = "idx_cluster", columnList = "cluster_id"),
                @Index(name = "idx_status", columnList = "status")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "任务表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"createdAt", "id"})
public class Job implements Serializable {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "任务ID")
    private Long id;


    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "所属租户ID")
    private Long tenantId;


    @Column(name = "cluster_id")
    @Schema(description = "所属集群ID，可为空")
    private Long clusterId;


    @Column(name = "job_name", nullable = false, length = 64)
    @Schema(description = "任务名")
    private String name;


    @Column(name = "job_type", length = 32)
    @Schema(description = "类型 (streaming/batch)")
    private String type;


    @Column(name = "engine", length = 16)
    @Schema(description = "计算引擎 (FLINK/SPARK)")
    private String engine;


    @Column(name = "env", length = 16)
    @Schema(description = "环境 (dev/staging/prod)")
    private String env;


    @Column(name = "status", nullable = false)
    @Schema(description = "状态：0=CREATED,1=RUNNING,2=FAILED,3=STOPPED,4=SUCCESS")
    private Short status;


    @Column(name = "owner_id")
    @Schema(description = "负责人ID")
    private Long ownerId;


    @Column(name = "start_time")
    @Schema(description = "启动时间")
    private LocalDateTime startTime;


    @Column(name = "end_time")
    @Schema(description = "结束时间")
    private LocalDateTime endTime;


    @Column(name = "spec", columnDefinition = "jsonb")
    @Schema(description = "JobSpec 快照（JSONB）")
    private String spec;


    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除；0=未删 1=已删")
    private Integer isDeleted = 0;


    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist(){ this.createdAt = LocalDateTime.now(); this.updatedAt = this.createdAt; }
    @PreUpdate
    public void preUpdate(){ this.updatedAt = LocalDateTime.now(); }
}