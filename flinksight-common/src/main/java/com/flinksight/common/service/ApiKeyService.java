package com.flinksight.common.service;

import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.dto.ApiKeyDTO;

import java.util.List;
import java.util.Optional;

public interface ApiKeyService extends SoftDeleteService<ApiKeyDTO, Long> {
    ApiKeyDTO createOrUpdate(ApiKeyDTO apiKey);
    Optional<ApiKeyDTO> getById(Long id);
    List<ApiKeyDTO> findByTenantId(Long tenantId);
    List<ApiKeyDTO> getAll();
}
