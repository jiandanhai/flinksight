package com.flinksight.common.service;

import com.flinksight.common.dto.ResourceDTO;


import java.util.List;
import java.util.Optional;

public interface ResourceService extends SoftDeleteService<ResourceDTO, Long> {
    ResourceDTO createOrUpdate(ResourceDTO entity);
    Optional<ResourceDTO> getById(Long id);
    List<ResourceDTO> getAll();
    List<ResourceDTO> findByTenantId(Long tenantId);
}
