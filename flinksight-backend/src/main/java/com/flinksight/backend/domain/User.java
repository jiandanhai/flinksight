package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
    private Long id;

    private Long tenantId;

    @Column(nullable = false, unique = true, length = 50)
    private String username; // 本地账号/SSO账号都可

    @Column(nullable = false, length = 128)
    private String password; // 密码HASH（本地用户必填，SSO可为空）

    private String nickname;
    private String email;
    private String phone;
    private String avatar;

    @Column(length = 128, unique = true, name = "sso_id")
    private String ssoId; // SSO唯一标识，三方用户用

    private Integer status = 1;
    private Integer isDeleted = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    // ===== UserDetails实现方法 =====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return roles; }
    @Override
    public String getUsername() { return username; }
    @Override
    public String getPassword() { return password; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return status != null && status == 1 && (isDeleted == null || isDeleted == 0); }
}