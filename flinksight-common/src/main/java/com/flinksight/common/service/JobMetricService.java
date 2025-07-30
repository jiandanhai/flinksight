package com.flinksight.common.service;


import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.dto.JobMetricDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 任务指标业务接口
 * JobMetric Service
 */
public interface JobMetricService extends SoftDeleteService<JobMetricDTO, Long>  {
    JobMetricDTO createMetric(JobMetricDTO metric);
    Optional<JobMetricDTO> getMetricById(Long id);
    List<JobMetricDTO> getMetricsByJob(Long jobId, LocalDateTime start, LocalDateTime end);
    List<JobMetricDTO> getMetricsByTenantAndMetric(Long tenantId, String metricKey, LocalDateTime start, LocalDateTime end);
}
