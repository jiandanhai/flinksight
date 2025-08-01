package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报警历史实体
 */
@Getter
@Setter
@Entity
@Table(name = "alert_history")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "报警历史表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class AlertHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "报警历史ID")
    private Long id;

    @Column(name = "alert_id", nullable = false)
    @Schema(description = "报警ID")
    private Long alertId;

    @Column(name = "rule_id")
    @Schema(description = "报警规则ID")
    private Long ruleId;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "报警内容")
    private String content;

    @Column(nullable = false)
    @Schema(description = "报警级别")
    private Integer level;

    @Column(nullable = false)
    @Schema(description = "处理状态")
    private Integer status;

    @Column(name = "operator_id")
    @Schema(description = "操作人ID")
    private Long operatorId;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "operate_time")
    @Schema(description = "操作时间")
    private LocalDateTime operateTime;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
