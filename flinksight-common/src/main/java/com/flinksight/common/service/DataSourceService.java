package com.flinksight.common.service;

import com.flinksight.common.dto.DataSourceDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface DataSourceService extends SoftDeleteService<DataSourceDTO, Long> {
    DataSourceDTO createOrUpdate(DataSourceDTO entity);
    Optional<DataSourceDTO> getById(Long id);
    PageResult<DataSourceDTO> findByTenantId(Long tenantId,int page, int size);
    PageResult<DataSourceDTO> getAll(int page, int size);
}
