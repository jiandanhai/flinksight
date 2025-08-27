package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作业指标表
 * 存储任务运行过程中的性能和监控指标
 */
@Getter
@Setter
@Entity
@Table(
        name = "job_metric",
        indexes = {
                @Index(name = "idx_jobmetric_tenant", columnList = "tenant_id"),
                @Index(name = "idx_jobmetric_jobid", columnList = "job_id"),
                @Index(name = "idx_jobmetric_key", columnList = "metric_key"),
                @Index(name = "idx_jobmetric_time", columnList = "metric_time"),
                @Index(name = "idx_jobmetric_jobkeytime", columnList = "job_id, metric_key, metric_time")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "任务指标表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"metricTime", "id"})
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
    @Schema(description = "指标类型（如cpu、mem、lag等）")
    private String metricKey;

    @Column(name = "metric_value")
    @Schema(description = "指标值")
    private Double value;

    @Column(name = "metric_time", nullable = false)
    @Schema(description = "采集时间")
    private LocalDateTime metricTime;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 (0=正常, 1=删除)")
    private Integer isDeleted = 0;
}
