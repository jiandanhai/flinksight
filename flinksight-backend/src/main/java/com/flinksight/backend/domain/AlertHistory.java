package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报警历史实体（含当时规则快照）
 * 用于记录报警生命周期中的每一次状态变更/处理动作，并保留当时命中的规则关键信息快照，
 * 确保规则后续变更不影响历史回溯。
 */
@Getter
@Setter
@Entity
@Table(
        name = "alert_history",
        indexes = {
                @Index(name = "idx_hist_tenant_alert_time", columnList = "tenant_id, alert_id, operate_time"),
                @Index(name = "idx_hist_rule", columnList = "rule_id"),
                @Index(name = "idx_hist_operator", columnList = "operator_id")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "报警历史表（含规则快照）")
@SQLRestriction("is_deleted=0") // 软删过滤
@DefaultSort(fields = {"createdAt", "id"})
public class AlertHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "报警历史ID")
    private Long id;

    // —— 归属维度 ——
    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "cluster_id", nullable = false)
    @Schema(description = "集群ID")
    private Long clusterId;

    // —— 关联主体（弱关联，无外键约束，便于软删/迁移） ——
    @Column(name = "alert_id", nullable = false)
    @Schema(description = "报警ID（弱关联 Alert）")
    private Long alertId;

    @Column(name = "rule_id")
    @Schema(description = "触发规则ID（弱关联 AlertRule，可空）")
    private Long ruleId;

    // —— 规则快照（写入时将命中规则的关键信息一并落表，便于历史回看） ——
    @Column(name = "rule_name", length = 128)
    @Schema(description = "规则名称快照")
    private String ruleName;

    @Column(name = "metric_key", length = 64)
    @Schema(description = "指标Key快照")
    private String metricKey;

    @Column(name = "threshold")
    @Schema(description = "阈值快照")
    private Double threshold;

    @Column(name = "compare_op", length = 8)
    @Schema(description = "比较符快照(>,<,=,!= 等)")
    private String compareOp;

    @Column(name = "channel", length = 32)
    @Schema(description = "通知渠道快照")
    private String channel;

    // —— 历史记录主体 ——
    @Column(columnDefinition = "TEXT")
    @Schema(description = "内容（如触发详情、处理备注等）")
    private String content;

    @Column(length = 16)
    @Schema(description = "报警级别（与 Alert 保持一致，如 INFO/WARN/CRITICAL）")
    private String level;

    @Column(nullable = false)
    @Schema(description = "处理状态(0未处理 1处理中 2关闭)")
    private Integer status = 0;

    @Column(name = "operator_id")
    @Schema(description = "操作人ID（处理/确认人员）")
    private Long operatorId;

    // —— 审计/软删 ——
    @Column(name = "operate_time")
    @Schema(description = "操作时间")
    private LocalDateTime operateTime;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "记录创建时间")
    private LocalDateTime createdAt;
}
