package com.flinksight.backend.security.tenant;

import java.lang.annotation.*;

/**
 * 标记方法/类为必须校验租户隔离（防串租）的业务点
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TenantRequired {
}