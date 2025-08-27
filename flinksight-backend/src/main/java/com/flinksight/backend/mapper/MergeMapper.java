package com.flinksight.backend.mapper;

import org.mapstruct.*;

public interface MergeMapper<D, E> extends GenericMapper<D, E> {

    /**
     * 将 dto 中【非 null】字段合并到 target。
     * dto 为 null 的字段不会覆盖 target 原值。
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void merge(D dto, @MappingTarget E target);
}