package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务日志实体
 * JobLog Entity
 */
@Data
@Entity
@Table(name = "job_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "任务日志表")
@Where(clause = "is_deleted=0")
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
    @Schema(description = "日志级别")
    private String level;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "日志内容")
    private String content;

    @Column
    @Schema(description = "日志产生时间")
    private LocalDateTime ts;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    @Schema(description = "软删除")
    private Integer isDeleted;
}
