package com.flinksight.common.service;

import com.flinksight.common.dto.IntegrationConfigDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface IntegrationConfigService extends SoftDeleteService<IntegrationConfigDTO, Long> {
    IntegrationConfigDTO createOrUpdate(IntegrationConfigDTO config);
    Optional<IntegrationConfigDTO> getById(Long id);
    PageResult<IntegrationConfigDTO> findByTenantId(Long tenantId,int page, int size);
    PageResult<IntegrationConfigDTO> getAll(int page, int size);
}
