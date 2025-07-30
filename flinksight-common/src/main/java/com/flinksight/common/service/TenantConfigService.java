package com.flinksight.common.service;

import com.flinksight.common.dto.TenantConfigDTO;

import java.util.List;
import java.util.Optional;

public interface TenantConfigService extends SoftDeleteService<TenantConfigDTO, Long> {
    TenantConfigDTO createOrUpdate(TenantConfigDTO config);

    Optional<TenantConfigDTO> getById(Long id);

    Optional<TenantConfigDTO> getByTenantIdAndConfigKey(Long tenantId, String configKey);

    List<TenantConfigDTO> listByTenantId(Long tenantId);

    boolean batchSoftDelete(List<Long> ids);
}
