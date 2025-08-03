package com.flinksight.common.service;

import com.flinksight.common.dto.ApiAccessLogDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface ApiAccessLogService extends SoftDeleteService<ApiAccessLogDTO, Long>  {
    ApiAccessLogDTO createOrUpdate(ApiAccessLogDTO log);
    Optional<ApiAccessLogDTO> getById(Long id);
    PageResult<ApiAccessLogDTO> findByTenantId(Long tenantId,int page, int size);
    PageResult<ApiAccessLogDTO> getAll(int page, int size);
}
