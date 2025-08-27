// GenericMapper.java
package com.flinksight.backend.mapper;

import com.flinksight.common.utils.BeanCopy;
import java.util.List;

/**
 * 通用DTO <-> Entity Mapper接口
 * 只要字段同名/兼容类型，自动完成转换；复杂字段在业务Mapper里补充@Mapping即可
 */
public interface GenericMapper<D, E> {
    D toDTO(E entity);
    E toEntity(D dto);

    List<D> toDTOList(List<E> entityList);
    List<E> toEntityList(List<D> dtoList);

    /** 仅把 dto 中“非 null”的字段合并到 entity（可额外忽略字段） */
    default void mergeIgnoreNull(D dto, E entity, String... ignoreProps) {
        BeanCopy.copyNonNull(dto, entity, ignoreProps);
    }

    /** 仅把 dto 中“非 null 且非空白字符串”的字段合并到 entity（可额外忽略字段） */
    default void mergeIgnoreNullAndBlank(D dto, E entity, String... ignoreProps) {
        BeanCopy.copyNonNullAndNonBlank(dto, entity, ignoreProps);
    }
}
