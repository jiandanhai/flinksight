package com.flinksight.common.service;

import com.flinksight.common.dto.TenantDTO;

import java.util.List;
import java.util.Optional;

/**
 * 租户业务接口
 * Tenant Service
 */
public interface TenantService  extends SoftDeleteService<TenantDTO, Long> {
    TenantDTO createTenant(TenantDTO tenant);
    Optional<TenantDTO> getTenantById(Long tenantId);
    Optional<TenantDTO> getTenantByCode(String code);
    List<TenantDTO> getAllTenants();
    TenantDTO updateTenant(TenantDTO tenant);
}
