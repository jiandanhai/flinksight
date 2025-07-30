package com.flinksight.common.service;


import com.flinksight.common.dto.LabelDTO;

import java.util.List;
import java.util.Optional;

public interface LabelService extends SoftDeleteService<LabelDTO, Long> {
    LabelDTO createOrUpdate(LabelDTO entity);
    Optional<LabelDTO> getById(Long id);
    List<LabelDTO> findByTenantId(Long tenantId);
}
