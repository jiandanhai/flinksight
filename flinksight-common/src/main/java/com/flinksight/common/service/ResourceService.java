package com.flinksight.common.service;

import com.flinksight.common.dto.ResourceDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface ResourceService extends SoftDeleteService<ResourceDTO, Long> {
    ResourceDTO createOrUpdate(ResourceDTO entity);
    Optional<ResourceDTO> getById(Long id);
    PageResult<ResourceDTO> getAll(int page, int size);
    PageResult<ResourceDTO> findByTenantId(Long tenantId,int page, int size);
}
