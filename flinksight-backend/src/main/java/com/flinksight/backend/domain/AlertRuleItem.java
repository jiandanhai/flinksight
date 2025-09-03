package com.flinksight.backend.domain;

import com.flinksight.common.enums.Aggregator;
import com.flinksight.common.enums.ComparatorOp;
import com.flinksight.common.enums.Severity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 规则项：表达指标/比较/阈值/窗口/聚合/等级/通知/自愈等
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alert_rule_item",
        uniqueConstraints = @UniqueConstraint(name = "uk_rule",
                columnNames = {"rule_set_id", "metric_key", "severity", "aggregator", "comparator", "window_seconds"}),
        indexes = @Index(name = "fk_rule_set", columnList = "rule_set_id")
)
@org.hibernate.annotations.SQLRestriction("is_deleted = 0") // 全局过滤掉软删除数据
public class AlertRuleItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_set_id", nullable = false)
    private Long ruleSetId;

    @Column(name = "metric_key", nullable = false, length = 64)
    private String metricKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "comparator", nullable = false, length = 8)
    private ComparatorOp comparator;

    @Column(name = "threshold",precision = 20, scale = 6)
    private BigDecimal threshold;

    @Column(name = "window_seconds", nullable = false)
    private Integer windowSeconds = 60;

    @Enumerated(EnumType.STRING)
    @Column(name = "aggregator", nullable = false, length = 8)
    private Aggregator aggregator = Aggregator.LAST;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false, length = 16)
    private Severity severity = Severity.WARN;

    @Column(name = "dedup_ms", nullable = false)
    private Integer dedupMs = 30000;

    @Column(name = "auto_recover", nullable = false)
    private Integer autoRecover = 0;

    @Column(name = "notify_policy_id")
    private Long notifyPolicyId;

    @Column(name = "enabled", nullable = false)
    private Integer enabled = 1;

    /* 软删除字段 */
    @Column(name="is_deleted", nullable=false) private Integer isDeleted = 0;
    @Column(name="deleted_at") private LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
