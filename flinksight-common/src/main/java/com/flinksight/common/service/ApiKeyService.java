package com.flinksight.common.service;

import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface ApiKeyService extends SoftDeleteService<ApiKeyDTO, Long> {
    ApiKeyDTO createOrUpdate(ApiKeyDTO apiKey);
    Optional<ApiKeyDTO> getById(Long id);
    PageResult<ApiKeyDTO> list(int page, int size);
}
