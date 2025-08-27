package com.flinksight.common.service;


import com.flinksight.common.dto.JobMetricDTO;
import com.flinksight.common.dto.MetricSeriesDTO;
import com.flinksight.common.model.PageResult;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 任务指标业务接口
 * JobMetric Service
 */
public interface JobMetricService extends SoftDeleteService<JobMetricDTO, Long>  {
    JobMetricDTO createMetric(JobMetricDTO metric);
    Optional<JobMetricDTO> getMetricById(Long id);
    MetricSeriesDTO getSeries(String metricKey, LocalDateTime from, LocalDateTime to);

    PageResult<JobMetricDTO> list(Long jobId, String metricKey,
                                       LocalDateTime start, LocalDateTime end,
                                       int page, int size);
}
