package com.flinksight.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.io.Serializable;

/**
 * 用户实体
 * 企业商用版 User Entity，实现Spring Security UserDetails
 */
@Data
@Entity
@Table(name = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户表")
@Where(clause = "is_deleted=0")
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
    @JsonIgnore // 密码不序列化
    @Schema(description = "密码HASH")
    private String password;

    @Column(length = 100)
    @Schema(description = "邮箱")
    private String email;

    @Column(length = 20)
    @Schema(description = "手机号")
    private String phone;

    @Column(nullable = false, columnDefinition = "tinyint default 1")
    @Schema(description = "状态(1启用0禁用)")
    private Integer status;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    @Schema(description = "软删除")
    private Integer isDeleted;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "注册时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @Schema(description = "角色集合")
    private List<Role> roles;

    /**
     * 用户权限集合
     * Spring Security会根据该集合进行权限判断
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    /**
     * 用户名，通常用于登录
     */
    @Override
    @JsonIgnore
    public String getUsername() {
        return this.username;
    }

    /**
     * 用户密码，框架自动验证
     */
    @Override
    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    /**
     * 账号是否未过期，true为可用
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否未锁定，true为可用
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        // 可以自定义锁定策略，示例默认全部可用
        return true;
    }

    /**
     * 密码是否未过期，true为可用
     */
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户是否启用（如 status 字段为1即启用）
     */
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return status != null && status == 1 && (isDeleted == null || isDeleted == 0);
    }
}
