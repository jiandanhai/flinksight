package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报警规则实体
 * AlertRule Entity
 */
@Data
@Entity
@Table(name = "alert_rule")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "报警规则表")
@Where(clause = "is_deleted=0")
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

    @Column(nullable = false, columnDefinition = "tinyint default 1")
    @Schema(description = "是否启用")
    private Integer enable;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    @Schema(description = "软删除")
    private Integer isDeleted;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
