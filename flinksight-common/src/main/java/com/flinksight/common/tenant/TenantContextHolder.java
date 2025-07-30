package com.flinksight.common.tenant;

public class TenantContextHolder {

    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();

    /**
     * 设置当前线程租户ID
     */
    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    /**
     * 获取当前线程租户ID
     */
    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    /**
     * 清除线程租户ID（请求结束后一定要调用，防止线程复用污染）
     */
    public static void clear() {
        TENANT_ID.remove();
    }
}
