package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 角色-权限关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "role_permission",
        uniqueConstraints = @UniqueConstraint(name="uk_rp_role_code_tenant", columnNames={"role_id","permission_code","tenant_id"}),
        indexes = {
                @Index(name = "idx_role_permission_role", columnList = "role_id"),
                @Index(name = "idx_role_permission_tenant", columnList = "tenant_id"),
                @Index(name = "idx_role_permission_code", columnList = "permission_code"),
        }
)
@Schema(description = "角色-权限关联表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"permissionCode", "id"})
public class RolePermission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "role_id", nullable = false)
    @Schema(description = "角色ID")
    private Long roleId;

    @Column(name="permission_code", length=50, nullable=false)
    private String permissionCode;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除 0=正常 1=删除")
    private Integer isDeleted = 0;

    /** 方便使用：基于 (code, tenant_id) 的关联（引用唯一键即可） */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="permission_code", referencedColumnName="code", insertable=false, updatable=false),
            @JoinColumn(name="tenant_id",      referencedColumnName="tenant_id", insertable=false, updatable=false)
    })
    private Permission permission;
}
