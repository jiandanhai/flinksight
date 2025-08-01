package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 租户-资源关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "tenant_resource",
        uniqueConstraints = @UniqueConstraint(name = "uk_tenant_resource", columnNames = {"tenant_id", "resource_id"}),
        indexes = {
                @Index(name = "idx_tenant_resource_tenant", columnList = "tenant_id"),
                @Index(name = "idx_tenant_resource_resource", columnList = "resource_id")
        }
)
@Schema(description = "租户-资源关联表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class TenantResource implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "resource_id", nullable = false)
    @Schema(description = "资源ID")
    private Long resourceId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
