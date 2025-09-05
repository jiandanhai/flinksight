package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(
        name = "`user`",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_sso_id", columnNames = {"sso_id"})
        },
        indexes = {
                @Index(name = "idx_user_tenant", columnList = "tenant_id"),
                @Index(name = "idx_user_status", columnList = "status"),
                @Index(name = "idx_user_updated_at", columnList = "updated_at"),
                @Index(name = "idx_user_idp_pair", columnList = "idp_issuer,idp_subject")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户影子表（Keycloak对接）")
@SQLRestriction("is_deleted=0")
@SQLDelete(sql = "UPDATE `user` SET is_deleted=1, updated_at=CURRENT_TIMESTAMP WHERE id=?")
@DefaultSort(fields = {"createdAt","id"})
@ToString(exclude = "roles")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    /** 默认/当前租户（真正授权建议以 user_role(tenant_id) 为准） */
    @Column(name = "tenant_id")
    private Long tenantId;

    /** 你现有业务使用的唯一键；建议写入 iss#sub */
    @Column(length = 256, unique = true, name = "sso_id", nullable = false)
    private String ssoId;

    /** 预留：多 realm 时可用 */
    @Column(name = "idp_issuer", length = 200)  private String idpIssuer;
    @Column(name = "idp_subject", length = 200) private String idpSubject;

    @Column(nullable = false, length = 80)   private String username;
    @Column(name = "display_name", length = 120) private String displayName;
    @Column(length = 160)                    private String email;
    @Column(name = "email_verified")         private Boolean emailVerified;
    @Column(length = 30)                     private String phone;
    @Column(length = 255)                    private String avatar;

    /** 不再使用本地口令，彻底移除即可；如保留历史列，也不要在代码中暴露 */
    // private String password;

    @Column(nullable = false)                private Integer status = 1;
    @Column(name = "is_deleted", nullable = false) private Integer isDeleted = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")                   private LocalDateTime updatedAt;
    @Column(name = "last_login_at")                private LocalDateTime lastLoginAt;

    // 过渡写法：系统级角色（若需要“租户内角色”，改成 UserRole 带 tenantId 的桥接表）
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    @BatchSize(size = 50)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    public boolean enabled() {
        return Objects.equals(status, 1) && Objects.equals(isDeleted, 0);
    }
}
