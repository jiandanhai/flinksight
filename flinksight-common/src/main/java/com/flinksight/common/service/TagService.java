package com.flinksight.common.service;

import com.flinksight.common.dto.TagDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface TagService extends SoftDeleteService<TagDTO, Long> {
    TagDTO createOrUpdate(TagDTO tag);
    Optional<TagDTO> getById(Long id);
    PageResult<TagDTO> getAll(int page, int size);
    PageResult<TagDTO> findByTenantId(Long tenantId,int page, int size);
}
