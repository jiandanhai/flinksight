package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 作业报警规则表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "job_alert_rule",
        indexes = {
                @Index(name = "idx_tenant", columnList = "tenant_id"),
                @Index(name = "idx_job_type", columnList = "job_type"),
                @Index(name = "idx_alert_type", columnList = "alert_type")
        }
)
@Schema(description = "作业报警规则表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"ruleName", "id"})
public class JobAlertRule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "规则ID")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "rule_name", nullable = false, length = 128)
    @Schema(description = "规则名称")
    private String ruleName;

    @Column(name = "job_type", length = 32)
    @Schema(description = "作业类型")
    private String jobType;

    @Column(name = "alert_type", length = 32)
    @Schema(description = "报警类型(EXCEPTION/DELAY/LAG)")
    private String alertType;

    @Column(name = "condition_json", columnDefinition = "TEXT")
    @Schema(description = "报警条件 (阈值/表达式JSON)")
    private String conditionJson;

    @Column(length = 255)
    @Schema(description = "通知目标 (如邮箱/手机号/钉钉群Webhook等)")
    private String target;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
