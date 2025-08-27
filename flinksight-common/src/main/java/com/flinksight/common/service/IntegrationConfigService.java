package com.flinksight.common.service;

import com.flinksight.common.dto.IntegrationConfigDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface IntegrationConfigService extends SoftDeleteService<IntegrationConfigDTO, Long> {
    IntegrationConfigDTO createOrUpdate(IntegrationConfigDTO config);
    Optional<IntegrationConfigDTO> getById(Long id);
    PageResult<IntegrationConfigDTO> list(int page, int size);
}
