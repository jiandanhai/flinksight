package com.flinksight.common.service;

import java.lang.annotation.*;

/**
 * 操作审计注解，标注敏感方法/接口自动记录日志
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpAudit {
    String action();            // 操作类型，如如 ALERT_CREATE / ALERT_DELETE …
    String targetType() default "";   // 对象类型，如 Alert / Rule / Tenant / User …
    String targetIdSpEL() default ""; // 支持SpEL表达式动态获取目标ID，例: "#id" / "#dto.id" / "#ret?.data?.id"
    String contentSpEL() default "";  // 支持SpEL动态记录内容 例: "'name='+#dto.name + ',enable='+#enable"
}