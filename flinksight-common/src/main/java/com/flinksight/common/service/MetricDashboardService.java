package com.flinksight.common.service;

import com.flinksight.common.dto.MetricDashboardDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface MetricDashboardService extends SoftDeleteService<MetricDashboardDTO, Long> {
    MetricDashboardDTO createOrUpdate(MetricDashboardDTO entity);
    Optional<MetricDashboardDTO> getById(Long id);
    PageResult<MetricDashboardDTO> findByTenantId(Long tenantId,int page, int size);
    PageResult<MetricDashboardDTO> getAll(int page, int size);
}
