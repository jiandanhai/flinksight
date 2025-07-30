package com.flinksight.common.security;

public class PermissionChecker {
    public static boolean hasConfigReadPermission(String operator, Long tenantId) {
        // 可接RBAC系统或租户表，DEMO返回true
        return true;
    }
    public static boolean hasConfigWritePermission(String operator, Long tenantId) {
        // 可接RBAC系统或租户表，DEMO返回true
        return true;
    }
}
