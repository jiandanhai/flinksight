package com.flinksight.common.service;

import com.flinksight.common.dto.ApiAccessLogDTO;
import com.flinksight.common.dto.DataSourceDTO;

import java.util.List;
import java.util.Optional;

public interface DataSourceService extends SoftDeleteService<DataSourceDTO, Long> {
    DataSourceDTO createOrUpdate(DataSourceDTO entity);
    Optional<DataSourceDTO> getById(Long id);
    List<DataSourceDTO> findByTenantId(Long tenantId);
    List<DataSourceDTO> getAll();
}
