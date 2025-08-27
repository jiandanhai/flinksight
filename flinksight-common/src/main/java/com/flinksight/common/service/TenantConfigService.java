package com.flinksight.common.service;

import com.flinksight.common.dto.TenantConfigDTO;
import com.flinksight.common.model.PageResult;

import java.util.List;
import java.util.Optional;

public interface TenantConfigService extends SoftDeleteService<TenantConfigDTO, Long> {
    TenantConfigDTO createOrUpdate(TenantConfigDTO config);

    Optional<TenantConfigDTO> getById(Long id);

    Optional<TenantConfigDTO> getByTenantIdAndConfigKey(Long tenantId, String configKey);

    PageResult<TenantConfigDTO> list(Long tenantId,int page, int size);

    boolean batchSoftDelete(Long tenantId,List<Long> ids);
}
