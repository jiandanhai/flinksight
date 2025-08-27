package com.flinksight.common.service;

import com.flinksight.common.dto.ResourceGroupDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface ResourceGroupService extends SoftDeleteService<ResourceGroupDTO, Long> {
    ResourceGroupDTO createOrUpdate(ResourceGroupDTO group);
    Optional<ResourceGroupDTO> getById(Long id);
    PageResult<ResourceGroupDTO> list(int page, int size);
}
