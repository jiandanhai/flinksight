package com.flinksight.common.service;

import com.flinksight.common.dto.ResourceLabelDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface ResourceLabelService extends SoftDeleteService<ResourceLabelDTO, Long>  {
    ResourceLabelDTO assignLabelToResource(Long resourceId, Long labelId);
    boolean removeLabelFromResource(Long resourceId, Long labelId);
    PageResult<ResourceLabelDTO> findByResourceId(Long resourceId,int page, int size);
    PageResult<ResourceLabelDTO> findByLabelId(Long labelId,int page, int size);
    Optional<ResourceLabelDTO> getById(Long id);
}
