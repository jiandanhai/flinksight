package com.flinksight.common.service;


import com.flinksight.common.dto.LabelDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface LabelService extends SoftDeleteService<LabelDTO, Long> {
    LabelDTO createOrUpdate(LabelDTO entity);
    Optional<LabelDTO> getById(Long id);
    PageResult<LabelDTO> findByTenantId(Long tenantId,int page, int size);
}
