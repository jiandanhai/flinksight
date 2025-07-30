package com.flinksight.common.service;

import com.flinksight.common.dto.ApiWhitelistDTO;

import java.util.Optional;

public interface ApiWhitelistService extends SoftDeleteService<ApiWhitelistDTO, Long> {
    ApiWhitelistDTO createOrUpdate(ApiWhitelistDTO entity);
    Optional<ApiWhitelistDTO> getById(Long id);
}
