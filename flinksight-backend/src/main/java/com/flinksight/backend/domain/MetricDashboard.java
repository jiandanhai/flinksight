package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

/**
 * 指标大盘实体
 * 支持多租户自定义大盘配置
 */
@Getter
@Setter
@Entity
@Table(
        name = "metric_dashboard",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_dashboard_tenant_name", columnNames = {"tenant_id", "name"})
        },
        indexes = {
                @Index(name = "idx_dashboard_tenant", columnList = "tenant_id"),
                @Index(name = "idx_dashboard_name", columnList = "name"),
                @Index(name = "idx_dashboard_ctime", columnList = "create_time")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "指标大盘")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class MetricDashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "大盘ID")
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    @Schema(description = "名称")
    private String name;

    @Column(name = "description", length = 256)
    @Schema(description = "描述")
    private String description;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "config", columnDefinition = "text")
    @Schema(description = "大盘配置(JSON)")
    private String config;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
