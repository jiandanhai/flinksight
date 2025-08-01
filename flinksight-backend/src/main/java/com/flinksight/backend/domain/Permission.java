package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 权限点实体
 * 商业级平台权限表，支持多租户、细粒度权限（菜单、按钮、API等）
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "permission",
        uniqueConstraints = @UniqueConstraint(name = "uk_permission_code_tenant", columnNames = {"code", "tenant_id"}),
        indexes = {
                @Index(name = "idx_permission_type", columnList = "type"),
                @Index(name = "idx_permission_tenant", columnList = "tenant_id")
        }
)
@Schema(description = "权限点表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class Permission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "权限ID")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "权限编码(全局唯一，如JOB_OWNER/ADMIN/VIEWER)")
    private String code;

    @Column(nullable = false, length = 50)
    @Schema(description = "权限名称")
    private String name;

    @Column(length = 100)
    @Schema(description = "权限描述")
    private String description;

    @Column(nullable = false, length = 16)
    @Schema(description = "权限类型(MENU/BUTTON/API)")
    private String type;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID，多租户隔离")
    private Long tenantId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标记 0=正常 1=删除")
    private Integer isDeleted = 0;
}
