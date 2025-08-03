package com.flinksight.common.service;

import com.flinksight.common.dto.OperationTemplateDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface OperationTemplateService extends SoftDeleteService<OperationTemplateDTO, Long> {
    OperationTemplateDTO createOrUpdate(OperationTemplateDTO template);
    Optional<OperationTemplateDTO> getById(Long id);
    PageResult<OperationTemplateDTO> getAll(int page, int size);
    PageResult<OperationTemplateDTO> findByTenantId(Long tenantId,int page, int size);
}
