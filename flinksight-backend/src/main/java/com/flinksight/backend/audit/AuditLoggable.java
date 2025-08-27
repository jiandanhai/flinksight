package com.flinksight.backend.audit;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLoggable {
    /** 必填：动作（如 "CREATE" / "UPDATE" / "DELETE" / "ENABLE"） */
    String action();

    /** 必填：对象类型（如 "ALERT" / "RULE" / "TENANT" / "USER" ...） */
    String target();

    /** 目标ID（SpEL），例如 "#id" / "#dto.id" / "#result.id" */
    String targetId() default "";

    /** 自定义详情（SpEL），例如 "'old='+#old+',new='+#dto" */
    String detail() default "";
}