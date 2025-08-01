package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作业诊断日志表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "job_diagnostic_log",
        indexes = {
                @Index(name = "idx_job", columnList = "job_id"),
                @Index(name = "idx_log_time", columnList = "log_time"),
                @Index(name = "idx_level", columnList = "level"),
                @Index(name = "idx_trace", columnList = "trace_id")
        }
)
@Schema(description = "作业诊断日志")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class JobDiagnosticLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "job_id", nullable = false)
    @Schema(description = "作业ID")
    private Long jobId;

    @Column(name = "job_name", length = 128)
    @Schema(description = "作业名称")
    private String jobName;

    @Column(name = "log_time", nullable = false)
    @Schema(description = "日志时间")
    private LocalDateTime logTime;

    @Column(length = 16)
    @Schema(description = "日志级别 (INFO/WARN/ERROR)")
    private String level;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "日志内容")
    private String content;

    @Column(name = "trace_id", length = 64)
    @Schema(description = "Trace ID (链路追踪)")
    private String traceId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 (0正常 1删除)")
    private Integer isDeleted = 0;
}
