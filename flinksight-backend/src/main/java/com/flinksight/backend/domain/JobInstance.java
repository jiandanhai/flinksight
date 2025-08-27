package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作业运行实例表
 * 每次调度/运行/补数/手动触发均产生一条记录
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "job_instance",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_instance_code", columnNames = {"instance_code"})
        },
        indexes = {
                @Index(name = "idx_jobinst_jobid", columnList = "job_id"),
                @Index(name = "idx_jobinst_status", columnList = "status"),
                @Index(name = "idx_jobinst_tenant", columnList = "tenant_id")
        }
)
@Schema(description = "作业实例表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"createdAt", "id"})
public class JobInstance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "job_id", nullable = false)
    @Schema(description = "作业定义ID，外键关联JobInfo")
    private Long jobId;

    @Column(name = "job_name", nullable = false, length = 128)
    @Schema(description = "作业名称快照")
    private String jobName;

    @Column(name = "engine_type", nullable = false, length = 32)
    @Schema(description = "作业类型（FLINK/SPARK等）")
    private String engineType;

    @Column(name = "cluster_id", nullable = false)
    @Schema(description = "运行集群ID")
    private Long clusterId;

    @Column(name = "instance_code", nullable = false, length = 128, unique = true)
    @Schema(description = "实例唯一标识（幂等/外部traceId）")
    private String instanceCode;

    @Column(name = "status", nullable = false)
    @Schema(description = "运行状态（0:CREATED/1:RUNNING/2:FAILED/3:STOPPED/4:RESTARTING/5:UNKNOWN等）")
    private Integer status;

    @Column(name = "trigger_type", nullable = false, length = 32)
    @Schema(description = "触发类型（SCHEDULE/MANUAL/RECOVERY/TEST等）")
    private String triggerType;

    @Column(name = "start_time")
    @Schema(description = "启动时间")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Column(name = "config_json", columnDefinition = "TEXT")
    @Schema(description = "运行参数/快照（JSON）")
    private String configJson;

    @Column(name = "result_json", columnDefinition = "TEXT")
    @Schema(description = "结果/产物快照（JSON）")
    private String resultJson;

    @Column(name = "fail_reason", columnDefinition = "TEXT")
    @Schema(description = "失败原因/异常堆栈")
    private String failReason;

    @Column(name = "retry_count", nullable = false)
    @Schema(description = "重试次数")
    private Integer retryCount = 0;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标记 0=正常 1=删除")
    private Integer isDeleted = 0;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "operator", length = 64)
    @Schema(description = "操作人")
    private String operator;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
