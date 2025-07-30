package com.flinksight.common.service;

import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.dto.MetricDashboardDTO;

import java.util.List;
import java.util.Optional;

public interface MetricDashboardService extends SoftDeleteService<MetricDashboardDTO, Long> {
    MetricDashboardDTO createOrUpdate(MetricDashboardDTO entity);
    Optional<MetricDashboardDTO> getById(Long id);
    List<MetricDashboardDTO> findByTenantId(Long tenantId);
    List<MetricDashboardDTO> getAll();
}
