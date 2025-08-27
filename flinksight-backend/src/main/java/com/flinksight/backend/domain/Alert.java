package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

// import 同上省略
@Getter
@Setter
@Entity
@Table(
        name = "`alert`",
        indexes = {
                @Index(name = "idx_alert_tenant_status_time", columnList = "tenant_id,status,created_at"),
                @Index(name = "idx_alert_rule", columnList = "rule_id")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "报警事件表")
@SQLRestriction("is_deleted=0")
@DefaultSort(fields = {"createdAt", "id"})
public class Alert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "cluster_id", nullable = false)
    @Schema(description = "集群ID")
    private Long clusterId;

    @Column(name = "job_id")
    private Long jobId;

    // ✅ 软关联到规则（可为空：也支持非规则型事件）
    @Column(name = "rule_id")
    private Long ruleId;

    // ✅ 规则快照（触发当时的关键信息，防止规则后续修改影响历史）
    @Column(name = "rule_name", length = 128)
    private String ruleName;

    @Column(name = "metric_key", length = 64)
    private String metricKey;

    @Column(name = "threshold")
    private Double threshold;

    @Column(name = "compare_op", length = 8)
    private String compareOp;

    @Column(name = "channel", length = 32)
    private String channel;

    @Column(length = 16)
    private String level;

    @Column(length = 32)
    private String type;

    @Column(length = 255)
    private String message;

    @Column(nullable = false)
    private Integer status = 0; // 0未处理 1处理中 2关闭

    @Column(name = "handler_id")
    private Long handlerId;

    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted = 0;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
