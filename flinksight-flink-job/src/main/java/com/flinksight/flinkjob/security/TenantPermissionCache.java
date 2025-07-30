package com.flinksight.flinkjob.security;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 租户/用户权限缓存同步(权限同步（Flink作业多租户与平台权限对接）)
 * 可结合平台API定时拉取权限、在流内实时隔离
 */
public class TenantPermissionCache {
    private static final Map<Long, Set<String>> permissionMap = new ConcurrentHashMap<>();

    public static void updateTenantPermissions(Long tenantId, Set<String> perms) {
        permissionMap.put(tenantId, perms);
    }
    public static boolean hasPermission(Long tenantId, String perm) {
        return permissionMap.getOrDefault(tenantId, Set.of()).contains(perm);
    }
}
