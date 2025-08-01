package com.flinksight.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.time.LocalDateTime;
import java.util.List;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "`user`") // ⚠️ user 是关键字，必须加反引号或改名
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "用户ID")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "所属租户ID")
    private Long tenantId;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "登录名")
    private String username;

    @Column(nullable = false, length = 128)
    @JsonIgnore
    @Schema(description = "密码HASH")
    private String password;

    @Column(length = 100)
    @Schema(description = "邮箱")
    private String email;

    @Column(length = 20)
    @Schema(description = "手机号")
    private String phone;

    @Column(nullable = false)
    @Schema(description = "状态(1启用0禁用)")
    private Integer status = 1; // 直接在Java端默认值

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除")
    private Integer isDeleted = 0;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "注册时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @Schema(description = "角色集合")
    private List<Role> roles;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return status != null && status == 1 && (isDeleted == null || isDeleted == 0);
    }
}
