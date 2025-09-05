package com.flinksight.backend.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

public final class SecurityUtil {
    private SecurityUtil() {}

    /** 取当前 Authentication，没有或类型不对直接抛 401 */
    public static UserPrincipal requirePrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new AuthenticationCredentialsNotFoundException("用户未认证");
        }
        Object p = auth.getPrincipal();
        if (p instanceof UserPrincipal up) return up;
        throw new AuthenticationCredentialsNotFoundException("非法认证主体类型: " + p.getClass().getSimpleName());
    }

    public static Long getCurrentUserId()   { return requirePrincipal().userId(); }
    public static String getCurrentSsoId()  { return requirePrincipal().ssoId(); }
    public static Long getCurrentTenantId() { return requirePrincipal().tenantId(); }
    public static String getCurrentUsername(){ return requirePrincipal().username(); }
    public static Set<String> getPermissions(){ return requirePrincipal().permissions(); }

    public static boolean hasAuthority(String code) {
        return getPermissions().contains(code);
    }
}