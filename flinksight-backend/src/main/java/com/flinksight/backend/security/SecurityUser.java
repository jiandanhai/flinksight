package com.flinksight.backend.security;

import com.flinksight.backend.domain.User;
import com.flinksight.common.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security上下文用户对象，支持扩展更多字段
 */
public class SecurityUser implements UserDetails {
    private final User user;
    private final List<String> permissionCodes;

    public SecurityUser(User user, List<String> permissionCodes) {
        this.user = user;
        this.permissionCodes = permissionCodes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissionCodes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getUsername(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return (UserStatus.ENABLED.getCode() == user.getStatus()); }

    public Long getTenantId() { return user.getTenantId(); }
    public Long getId() { return user.getId(); }
    public User getUser() { return user; }
    public List<String> getPermissionCodes() { return permissionCodes; }
}
