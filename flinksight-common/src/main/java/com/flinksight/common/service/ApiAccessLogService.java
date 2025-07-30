package com.flinksight.common.service;

import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.dto.ApiAccessLogDTO;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

public interface ApiAccessLogService extends SoftDeleteService<ApiAccessLogDTO, Long>  {
    ApiAccessLogDTO createOrUpdate(ApiAccessLogDTO log);
    Optional<ApiAccessLogDTO> getById(Long id);
    List<ApiAccessLogDTO> findByTenantId(Long tenantId);
    List<ApiAccessLogDTO> getAll();
}
