package com.flinksight.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知策略：承载渠道 JSON 与限频等参数
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alert_notify_policy",
        uniqueConstraints = @UniqueConstraint(name = "uk_tenant_name", columnNames = {"tenant_id", "name"})
)
public class AlertNotifyPolicy implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Lob
    @Column(name = "channels", nullable = false, columnDefinition = "json")
    private String channels; // JSON 字符串

    @Column(name = "rate_limit_per_min")
    private Integer rateLimitPerMin;
    @Column(name = "created_by", nullable = false, length = 64)
    private String createdBy;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
