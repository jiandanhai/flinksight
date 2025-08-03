// GenericMapper.java
package com.flinksight.backend.mapper;

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
}
