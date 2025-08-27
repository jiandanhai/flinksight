package com.flinksight.common.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public final class BeanCopy {

    private BeanCopy() {}

    /** 只拷贝 source 中非 null 的字段到 target；支持额外忽略字段 */
    public static void copyNonNull(Object source, Object target, String... ignoreProps) {
        String[] ignore = union(nullPropertyNames(source), ignoreProps);
        BeanUtils.copyProperties(source, target, ignore);
    }

    /** 只拷贝 source 中非 null 且非空白字符串 的字段到 target；支持额外忽略字段 */
    public static void copyNonNullAndNonBlank(Object source, Object target, String... ignoreProps) {
        String[] ignoreNull = nullPropertyNames(source);
        String[] ignoreBlank = blankStringPropertyNames(source);
        String[] ignore = union(ignoreNull, union(ignoreBlank, ignoreProps));
        BeanUtils.copyProperties(source, target, ignore);
    }

    // --------------- helpers ---------------

    private static String[] nullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : src.getPropertyDescriptors()) {
            Object val = src.getPropertyValue(pd.getName());
            if (val == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }

    private static String[] blankStringPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        Set<String> names = new HashSet<>();
        for (PropertyDescriptor pd : src.getPropertyDescriptors()) {
            Object val = src.getPropertyValue(pd.getName());
            if (val instanceof String s && s.isBlank()) {
                names.add(pd.getName());
            }
        }
        return names.toArray(new String[0]);
    }

    private static String[] union(String[] a, String[] b) {
        Set<String> set = new HashSet<>();
        if (a != null) for (String s : a) set.add(s);
        if (b != null) for (String s : b) set.add(s);
        return set.toArray(new String[0]);
    }
}