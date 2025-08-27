package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 第三方集成配置
 */
@Getter
@Setter
@Entity
@Table(
        name = "integration_config",
        indexes = {
                @Index(name = "idx_tenant", columnList = "tenant_id"),
                @Index(name = "idx_type", columnList = "type"),
                @Index(name = "idx_status", columnList = "status")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "第三方集成配置表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"createdAt", "id"})
public class IntegrationConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(nullable = false, length = 128)
    @Schema(description = "集成名称")
    private String name;

    @Column(nullable = false, length = 64)
    @Schema(description = "集成类型")
    private String type;

    @Column(name = "config_json", columnDefinition = "TEXT")
    @Schema(description = "配置参数(JSON)")
    private String configJson;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(nullable = false)
    @Schema(description = "启用状态 (1启用 0禁用)")
    private Integer status = 1; // 默认启用

    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
