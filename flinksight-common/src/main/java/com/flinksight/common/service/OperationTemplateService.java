package com.flinksight.common.service;

import com.flinksight.common.dto.OperationTemplateDTO;

import java.util.List;
import java.util.Optional;

public interface OperationTemplateService extends SoftDeleteService<OperationTemplateDTO, Long> {
    OperationTemplateDTO createOrUpdate(OperationTemplateDTO template);
    Optional<OperationTemplateDTO> getById(Long id);
    List<OperationTemplateDTO> getAll();
    List<OperationTemplateDTO> findByTenantId(Long tenantId);
}
