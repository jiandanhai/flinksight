package com.flinksight.common.service;

import com.flinksight.common.dto.MetricDashboardDTO;
import com.flinksight.common.dto.NodeMetricResponseDTO;
import com.flinksight.common.model.PageResult;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MetricDashboardService extends SoftDeleteService<MetricDashboardDTO, Long> {
    MetricDashboardDTO createOrUpdate(MetricDashboardDTO entity);
    Optional<MetricDashboardDTO> getById(Long id);
    PageResult<MetricDashboardDTO> list(int page, int size);

    NodeMetricResponseDTO getNodeMetric(Long clusterId, LocalDateTime from, LocalDateTime to, String agg);
}
