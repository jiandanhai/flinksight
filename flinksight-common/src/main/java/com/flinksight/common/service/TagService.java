package com.flinksight.common.service;

import com.flinksight.common.dto.TagDTO;

import java.util.List;
import java.util.Optional;

public interface TagService extends SoftDeleteService<TagDTO, Long> {
    TagDTO createOrUpdate(TagDTO tag);
    Optional<TagDTO> getById(Long id);
    List<TagDTO> getAll();
    List<TagDTO> findByTenantId(Long tenantId);
}
