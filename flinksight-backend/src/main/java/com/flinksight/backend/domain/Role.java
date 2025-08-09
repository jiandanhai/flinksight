package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 角色实体
 * Role Entity
 */
@Getter
@Setter
@Entity
@Table(
        name = "role",
        uniqueConstraints = @UniqueConstraint(name = "uk_code", columnNames = {"code"}),
        indexes = {
                @Index(name = "idx_tenant", columnList = "tenant_id")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色表")
@SQLRestriction("is_deleted=0") // 软删除
public class Role implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "角色ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "角色编码")
    private String code;  // 统一用code，原roleCode

    @Column(nullable = false, length = 50)
    @Schema(description = "角色名称")
    private String name;  // 原roleName

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(length = 100)
    @Schema(description = "角色描述")
    private String remark; // 原desc

    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @Schema(description = "用户集合")
    private List<User> users;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标记 0=正常 1=删除")
    private Integer isDeleted = 0;

    // Spring Security 权限接口
    @Override
    public String getAuthority() {
        return code;
    }
}
