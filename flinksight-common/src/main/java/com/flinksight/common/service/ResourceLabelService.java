package com.flinksight.common.service;

import com.flinksight.common.dto.ResourceLabelDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface ResourceLabelService extends SoftDeleteService<ResourceLabelDTO, Long>  {
    ResourceLabelDTO assignLabelToResource(Long resourceId, Long labelId);
    boolean removeLabelFromResource(Long resourceId, Long labelId);
    Optional<ResourceLabelDTO> getById(Long id);

    PageResult<ResourceLabelDTO> list(Long resourceId, Long labelId, int page, int size);
}
