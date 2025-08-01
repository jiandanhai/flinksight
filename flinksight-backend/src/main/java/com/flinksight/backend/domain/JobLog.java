package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作业日志表
 * 存储作业运行过程中的详细日志
 */
@Getter
@Setter
@Entity
@Table(
        name = "job_log",
        indexes = {
                @Index(name = "idx_joblog_tenant", columnList = "tenant_id"),
                @Index(name = "idx_joblog_jobid", columnList = "job_id"),
                @Index(name = "idx_joblog_level", columnList = "level"),
                @Index(name = "idx_joblog_time", columnList = "log_time")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "任务日志表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class JobLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "job_id", nullable = false)
    @Schema(description = "任务ID")
    private Long jobId;

    @Column(length = 16)
    @Schema(description = "日志级别 (INFO/WARN/ERROR)")
    private String level;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "日志内容")
    private String content;

    @Column(name = "log_time", nullable = false)
    @Schema(description = "日志产生时间")
    private LocalDateTime logTime;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;
}
