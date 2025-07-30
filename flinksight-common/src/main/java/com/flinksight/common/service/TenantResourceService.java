package com.flinksight.common.service;


import com.flinksight.common.dto.TenantResourceDTO;

import java.util.List;
import java.util.Optional;

public interface TenantResourceService extends SoftDeleteService<TenantResourceDTO, Long> {
    TenantResourceDTO assignResourceToTenant(Long tenantId, Long resourceId);
    boolean removeResourceFromTenant(Long tenantId, Long resourceId);
    List<TenantResourceDTO> findByTenantId(Long tenantId);
    List<TenantResourceDTO> findByResourceId(Long resourceId);
    Optional<TenantResourceDTO> getById(Long id);
}
