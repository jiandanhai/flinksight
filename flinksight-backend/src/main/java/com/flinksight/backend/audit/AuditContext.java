package com.flinksight.backend.audit;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

public final class AuditContext {
    private AuditContext() {}

    public static Long tenantId() {
        // 你的多租户策略：可从 ThreadLocal/TenantFilter/MDC/Header 获取
        String v = MDC.get("tenantId");
        if (v == null) v = getRequestHeader("X-Tenant-Id");
        return v == null ? 0L : parseLong(v);
    }

    public static Long userId() {
        String v = MDC.get("userId");
        if (v == null) v = getRequestHeader("X-User-Id");
        if (v != null) return parseLong(v);

        // 或从 Spring Security 取
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User u) {
            // 这里示例：用户名可解析ID；你按项目改造
            return 0L;
        }
        return 0L;
    }

    public static String operator() {
        String v = MDC.get("operator");
        if (v == null) v = getRequestHeader("X-Operator");
        if (v != null) return v;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) return String.valueOf(auth.getName());
        return "system";
    }

    public static String clientIp() {
        HttpServletRequest req = currentRequest();
        if (req == null) return "0.0.0.0";
        String ip = Optional.ofNullable(req.getHeader("X-Forwarded-For")).orElse(req.getRemoteAddr());
        // 只取第一个
        int i = ip.indexOf(',');
        return i > 0 ? ip.substring(0, i).trim() : ip;
    }

    public static String traceId() {
        String v = MDC.get("traceId");
        if (v == null) v = getRequestHeader("X-Trace-Id");
        return v != null ? v : UUID.randomUUID().toString().replace("-", "");
    }

    private static HttpServletRequest currentRequest() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) return sra.getRequest();
        return null;
    }

    private static String getRequestHeader(String name) {
        HttpServletRequest req = currentRequest();
        return req == null ? null : req.getHeader(name);
    }

    private static long parseLong(String v) {
        try { return Long.parseLong(v); } catch (Exception e) { return 0L; }
    }
}