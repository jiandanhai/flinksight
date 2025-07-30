package com.flinksight.backend.security.rbac;

import java.lang.annotation.*;

/**
 * 权限点注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpPermission {
    /**
     * 权限编码（如"user:delete"，"job:export"等）
     */
    String value();
}
