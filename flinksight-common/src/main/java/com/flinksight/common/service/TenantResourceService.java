package com.flinksight.common.service;


import com.flinksight.common.dto.TenantResourceDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface TenantResourceService extends SoftDeleteService<TenantResourceDTO, Long> {
    TenantResourceDTO assignResourceToTenant(Long tenantId, Long resourceId);
    boolean removeResourceFromTenant(Long tenantId, Long resourceId);
    PageResult<TenantResourceDTO> findByTenantId(Long tenantId,int page, int size);
    PageResult<TenantResourceDTO> findByResourceId(Long resourceId,int page, int size);
    Optional<TenantResourceDTO> getById(Long id);
}
