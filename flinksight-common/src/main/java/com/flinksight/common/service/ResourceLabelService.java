package com.flinksight.common.service;

import com.flinksight.common.dto.ResourceLabelDTO;

import java.util.List;
import java.util.Optional;

public interface ResourceLabelService extends SoftDeleteService<ResourceLabelDTO, Long>  {
    ResourceLabelDTO assignLabelToResource(Long resourceId, Long labelId);
    boolean removeLabelFromResource(Long resourceId, Long labelId);
    List<ResourceLabelDTO> findByResourceId(Long resourceId);
    List<ResourceLabelDTO> findByLabelId(Long labelId);
    Optional<ResourceLabelDTO> getById(Long id);
}
