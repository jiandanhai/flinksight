package com.flinksight.common.service;

import com.flinksight.common.dto.ResourceDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface ResourceService extends SoftDeleteService<ResourceDTO, Long> {
    ResourceDTO createOrUpdate(ResourceDTO entity);
    Optional<ResourceDTO> getById(Long id);
    PageResult<ResourceDTO> list(int page, int size);
}
