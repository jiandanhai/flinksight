package com.flinksight.backend.domain;

import lombok.*;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作业信息实体
 * 作用：代表“平台管理的某一个逻辑作业/流程/任务”的唯一身份与基本配置。
 * 只存一份，每个作业一条，对应开发/运维管理视角下的“作业/脚本/调度模板”。
 * 典型字段：
 * jobName（作业名/唯一键）
 * tenantId、projectCode
 * jobType、owner、权限信息
 * 调度参数、报警策略、默认配置等
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "job_info",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_jobinfo_jobname_tenant", columnNames = {"job_name", "tenant_id"}),
        @UniqueConstraint(name = "uk_jobinfo_traceid", columnNames = {"trace_id"})
    },
    indexes = {
        @Index(name = "idx_jobinfo_tenant", columnList = "tenant_id"),
        @Index(name = "idx_jobinfo_type", columnList = "job_type")
    }
)
public class JobInfo  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_name", nullable = false, length = 128)
    private String jobName;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "job_type", nullable = false, length = 32)
    private String jobType;

    @Column(name = "project_code", length = 64)
    private String projectCode;

    @Column(name = "operator", nullable = false, length = 64)
    private String operator;

    @Column(name = "source", nullable = false, length = 32)
    private String source;

    @Column(name = "trace_id", nullable = false, unique = true, length = 64)
    private String traceId;

    @Column(name = "remark", length = 256)
    private String remark;

    @Column(name = "register_at", nullable = false)
    private Long registerAt;

    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
