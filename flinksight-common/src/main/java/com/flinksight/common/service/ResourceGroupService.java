package com.flinksight.common.service;

import com.flinksight.common.dto.ResourceGroupDTO;

import java.util.List;
import java.util.Optional;

public interface ResourceGroupService extends SoftDeleteService<ResourceGroupDTO, Long> {
    ResourceGroupDTO createOrUpdate(ResourceGroupDTO group);
    Optional<ResourceGroupDTO> getById(Long id);
    List<ResourceGroupDTO> getAll();
    List<ResourceGroupDTO> findByTenantId(Long tenantId);
}
