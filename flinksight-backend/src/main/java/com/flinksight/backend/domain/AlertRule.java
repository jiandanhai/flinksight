package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报警规则实体
 * AlertRule Entity
 */
@Getter
@Setter
@Entity
@Table(
        name = "alert_rule",
        indexes = {
                @Index(name = "idx_tenant", columnList = "tenant_id"),
                @Index(name = "idx_cluster", columnList = "cluster_id")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "报警规则表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class AlertRule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "cluster_id", nullable = false)
    @Schema(description = "集群ID")
    private Long clusterId;

    @Column(name = "metric_key", length = 64)
    @Schema(description = "指标")
    private String metricKey;

    @Column
    @Schema(description = "阈值")
    private Double threshold;

    @Column(name = "compare_op", length = 8)
    @Schema(description = "比较符(>,<,=,!=等)")
    private String compareOp;

    @Column(length = 32)
    @Schema(description = "通知方式")
    private String channel;

    @Column(nullable = false)
    @Schema(description = "是否启用")
    private Integer enable = 1; // ⚡ 用 Java 默认值

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除")
    private Integer isDeleted = 0;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
