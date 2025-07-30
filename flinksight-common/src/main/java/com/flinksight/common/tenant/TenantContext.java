package com.flinksight.common.tenant;

/**
 * 多租户上下文工具
 * 多租户上下文
 */
public class TenantContext {
    private static final ThreadLocal<Long> context = new ThreadLocal<>();
    public static void setTenantId(Long tenantId) { context.set(tenantId);}
    public static Long getTenantId() { return context.get();}
    public static void clear() { context.remove();}
}
