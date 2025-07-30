package com.flinksight.backend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.List;
import com.flinksight.backend.domain.User;

public class SecurityUser implements UserDetails {
    private final User user;
    private final List<String> permissionCodes; // 权限码列表

    public SecurityUser(User user, List<String> permissionCodes) {
        this.user = user;
        this.permissionCodes = permissionCodes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissionCodes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return "ENABLED".equals(user.getStatus()); }
    public Long getTenantId() { return user.getTenantId(); }
    public Long getId() { return user.getId(); }
}
