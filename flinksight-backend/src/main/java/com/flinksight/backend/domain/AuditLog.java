package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作审计实体
 * AuditLog Entity
 */
@Getter
@Setter
@Entity
@Table(
        name = "audit_log",
        indexes = {
                @Index(name = "idx_tenant", columnList = "tenant_id"),
                @Index(name = "idx_trace", columnList = "trace_id"),
                @Index(name = "idx_operator", columnList = "operator")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "操作审计日志表")
@SQLRestriction("is_deleted=0")  // 替代 Hibernate 6.3 的 @Where
public class AuditLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "user_id")
    @Schema(description = "操作人ID")
    private Long userId;

    @Column(length = 64)
    @Schema(description = "操作类型")
    private String action;

    @Column(name = "target_type", length = 32)
    @Schema(description = "对象类型")
    private String targetType;

    @Column(name = "target_id")
    @Schema(description = "对象ID")
    private Long targetId;

    @Column(length = 45)
    @Schema(description = "IP地址")
    private String ip;

    @Column(length = 255)
    @Schema(description = "操作内容")
    private String content;

    @Column(name = "operator", length = 64, nullable = false)
    @Schema(description = "操作人用户名/ID")
    private String operator;

    @Column(name = "trace_id", length = 64)
    @Schema(description = "全链路追踪ID")
    private String traceId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "操作时间")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除")
    private Integer isDeleted = 0;
}
