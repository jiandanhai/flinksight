package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Where;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作业诊断日志表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_diagnostic_log")
@Schema(description = "作业诊断日志")
@Where(clause = "is_deleted=0")
public class JobDiagnosticLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long jobId;

    private String jobName;

    private LocalDateTime logTime;

    private String level; // INFO/WARN/ERROR

    private String content;

    private String traceId;

    private Integer isDeleted;
}
