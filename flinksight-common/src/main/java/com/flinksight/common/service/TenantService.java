package com.flinksight.common.service;

import com.flinksight.common.dto.TenantDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

/**
 * 租户业务接口
 * Tenant Service
 */
public interface TenantService  extends SoftDeleteService<TenantDTO, Long> {
    TenantDTO createTenant(TenantDTO tenant);
    Optional<TenantDTO> getTenantById(Long tenantId);
    Optional<TenantDTO> getTenantByCode(String code);
    PageResult<TenantDTO> getAllTenants(int page, int size);
    TenantDTO updateTenant(TenantDTO tenant);
}
