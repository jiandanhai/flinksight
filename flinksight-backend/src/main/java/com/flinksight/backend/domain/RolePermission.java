package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;

/**
 * 角色-权限关联表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role_permission", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"role_id", "permission_id"})
})
@Schema(description = "角色-权限关联表")
@Where(clause = "is_deleted=0")
public class RolePermission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "role_id", nullable = false)
    @Schema(description = "角色ID")
    private Long roleId;

    @Column(name = "permission_id", nullable = false)
    @Schema(description = "权限ID")
    private Long permissionId;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    @Schema(description = "软删除")
    private Integer isDeleted;
}
