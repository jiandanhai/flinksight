package com.flinksight.common.service;

import com.flinksight.common.dto.DataSourceDTO;
import com.flinksight.common.dto.IntegrationConfigDTO;

import java.util.List;
import java.util.Optional;

public interface IntegrationConfigService extends SoftDeleteService<IntegrationConfigDTO, Long> {
    IntegrationConfigDTO createOrUpdate(IntegrationConfigDTO config);
    Optional<IntegrationConfigDTO> getById(Long id);
    List<IntegrationConfigDTO> findByTenantId(Long tenantId);
    List<IntegrationConfigDTO> getAll();
}
