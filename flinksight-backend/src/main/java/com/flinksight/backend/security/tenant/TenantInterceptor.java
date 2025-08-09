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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取请求头中的租户ID
        String tenantId = request.getHeader("X-Tenant-Id");

        if (tenantId != null) {
            // 设置租户ID到 TenantContext 中
            TenantContext.setTenantId(Long.parseLong(tenantId));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理租户ID
        TenantContext.clear();
    }
}
