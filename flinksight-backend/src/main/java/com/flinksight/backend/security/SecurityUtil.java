package com.flinksight.backend.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

public class SecurityUtil {
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser)) {
            throw new RuntimeException("用户未认证");
        }
        SecurityUser principal = (SecurityUser) authentication.getPrincipal();
        return principal.getId();
    }
    public static Long getCurrentTenantId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser)) {
            throw new RuntimeException("用户未认证");
        }
        SecurityUser principal = (SecurityUser) authentication.getPrincipal();
        return principal.getTenantId();
    }
}