// package com.flinksight.backend.domain
package com.flinksight.backend.domain;

import com.flinksight.common.enums.RuleSetStatus;
import com.flinksight.common.enums.ScopeType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 规则集（版本 + 作用域 + 激活位）
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alert_rule_set",
        uniqueConstraints = @UniqueConstraint(name = "uk_scope_ver", columnNames = {"tenant_id", "scope_type", "scope_id", "version"}),
        indexes = @Index(name = "idx_scope_active", columnList = "tenant_id,scope_type,scope_id,active_flag")
)
public class AlertRuleSet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope_type", nullable = false, length = 16)
    private ScopeType scopeType;

    @Column(name = "scope_id")
    private Long scopeId; // TENANT 级可为 null

    @Column(name = "version", nullable = false)
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private RuleSetStatus status = RuleSetStatus.DRAFT;

    @Column(name = "active_flag", nullable = false)
    private Integer activeFlag = 0;

    @Column(name = "checksum", length = 64)
    private String checksum;

    @Column(name = "created_by", nullable = false, length = 64)
    private String createdBy;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
