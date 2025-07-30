package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作用：每一次实际运行、调度、触发后的“实例化过程”。
 * 一条JobInfo可以有成千上万个JobInstance，每次调度/触发/补数/补救/测试都会产生一次实例快照。
 * 典型字段：
 * instanceId（实例ID/主键，自增/雪花ID/uuid）
 * jobId（外键，关联JobInfo）
 * tenantId
 * 调度触发类型（自动/手动/补数/自愈）
 * 运行状态（WAITING、RUNNING、FAILED、SUCCEEDED、KILLED…）
 * 启动参数、运行资源、报警快照、链路追踪、操作人
 * startTime/endTime、持续时长、日志位置、异常原因
 * 本次实例的自定义指标、输出结果/产物等
 * 数据量远大于JobInfo，归档/高可用/多级运维必须分表/分库管理。
 * 作业运行实例表（每次调度/运行/补数/手动等都产生一条）
 * 用于运维、监控、链路追踪、告警、自愈、历史统计、权限等
 * jobId：外键，关联 JobInfo（作业定义主表）。
 * instanceCode：平台唯一实例标识，可用于外部traceId、幂等、第三方回调唯一性。
 * status/triggerType：支持批量调度、手动/自动/自愈/回补等场景。
 * configJson/resultJson：记录本次实例的参数快照和产物，方便链路追踪与回溯。
 * failReason/retryCount：为运维自愈、失败排查、自动补偿等提供原生支撑。
 * operator：支持多租户多用户权限审计。
 * createdAt/updatedAt：便于归档和数据生命周期管理。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "job_instance",
        indexes = {
                @Index(name = "idx_jobinst_jobid", columnList = "job_id"),
                @Index(name = "idx_jobinst_status", columnList = "status"),
                @Index(name = "idx_jobinst_tenant", columnList = "tenant_id")
        }
)
@Schema(description = "作业实例表")
@Where(clause = "is_deleted=0")
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

    @Column(name = "instance_code", nullable = false, length = 128)
    @Schema(description = "实例唯一标识（可用于幂等/外部traceId）")
    private String instanceCode;

    @Column(name = "status", nullable = false, length = 32)
    @Schema(description = "运行状态（INIT/RUNNING/FAILED/SUCCESS/KILLED等）")
    private String status;

    @Column(name = "trigger_type", nullable = false, length = 32)
    @Schema(description = "调度触发类型（SCHEDULE/MANUAL/RECOVERY/TEST等）")
    private String triggerType;

    @Column(name = "start_time")
    @Schema(description = "启动时间")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Column(name = "config_json", columnDefinition = "TEXT")
    @Schema(description = "作业运行时动态参数/配置（JSON）")
    private String configJson;

    @Column(name = "result_json", columnDefinition = "TEXT")
    @Schema(description = "作业输出/统计/产物等（JSON）")
    private String resultJson;

    @Column(name = "fail_reason", length = 512)
    @Schema(description = "失败原因/异常堆栈")
    private String failReason;

    @Column(name = "retry_count", nullable = false)
    @Schema(description = "自动重试次数")
    private Integer retryCount;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标记 0=正常 1=删除")
    private Integer isDeleted;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "operator", length = 64)
    @Schema(description = "触发操作人/调度人")
    private String operator;

    @Column(name = "created_at")
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
