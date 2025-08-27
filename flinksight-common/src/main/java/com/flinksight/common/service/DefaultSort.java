package com.flinksight.common.service;

import org.springframework.data.domain.Sort;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DefaultSort {
    /** 默认排序字段，按先后顺序应用 */
    String[] fields();
    /** 默认方向，默认 DESC */
    Sort.Direction direction() default Sort.Direction.DESC;
}