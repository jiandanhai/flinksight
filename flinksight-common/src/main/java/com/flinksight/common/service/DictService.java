package com.flinksight.common.service;

import com.flinksight.common.dto.DictDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface DictService extends SoftDeleteService<DictDTO, Long> {
    DictDTO createOrUpdate(DictDTO entity);
    Optional<DictDTO> getById(Long id);
    PageResult<DictDTO> findByDictType(String dictType,int page, int size);
}
