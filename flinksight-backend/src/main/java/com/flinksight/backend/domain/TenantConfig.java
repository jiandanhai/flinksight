package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 租户配置表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "tenant_config",
        uniqueConstraints = @UniqueConstraint(name = "uk_tenant_config_key", columnNames = {"tenant_id", "config_key"}),
        indexes = {
                @Index(name = "idx_tenant_config_tenant", columnList = "tenant_id"),
                @Index(name = "idx_tenant_config_key", columnList = "config_key")
        }
)
@Schema(description = "租户配置表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"createTime", "id"})
public class TenantConfig implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "config_key", nullable = false, length = 64)
    @Schema(description = "配置项Key")
    private String configKey;

    @Column(name = "config_value", columnDefinition = "text")
    @Schema(description = "配置值")
    private String configValue;

    @Column(name = "description", length = 256)
    @Schema(description = "说明")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
