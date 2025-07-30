package com.flinksight.common.service;

import com.flinksight.common.dto.DictDTO;

import java.util.List;
import java.util.Optional;

public interface DictService extends SoftDeleteService<DictDTO, Long> {
    DictDTO createOrUpdate(DictDTO entity);
    Optional<DictDTO> getById(Long id);
    List<DictDTO> findByDictType(String dictType);
}
