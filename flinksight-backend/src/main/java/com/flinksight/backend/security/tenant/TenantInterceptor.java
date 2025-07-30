package com.flinksight.backend.security.tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 多租户拦截器
 * 自动注入租户ID到上下文
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {

    public static final ThreadLocal<Long> TENANT_CTX = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantId = request.getHeader("X-Tenant-Id");
        if (tenantId != null) {
            TENANT_CTX.set(Long.parseLong(tenantId));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TENANT_CTX.remove();
    }

    public static Long getCurrentTenantId() {
        return TENANT_CTX.get();
    }
}
