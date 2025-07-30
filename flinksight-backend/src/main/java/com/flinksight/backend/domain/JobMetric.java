package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务指标实体
 * JobMetric Entity
 */
@Data
@Entity
@Table(name = "job_metric")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "任务指标表")
@Where(clause = "is_deleted=0")
public class JobMetric implements Serializable {

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

    @Column(name = "metric_key", nullable = false, length = 64)
    @Schema(description = "指标类型，如'cpu','mem','lag'等")
    private String metricKey;

    @Column
    @Schema(description = "指标值")
    private Double value;

    @Column
    @Schema(description = "采集时间")
    private LocalDateTime ts;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    @Schema(description = "软删除")
    private Integer isDeleted;
}
