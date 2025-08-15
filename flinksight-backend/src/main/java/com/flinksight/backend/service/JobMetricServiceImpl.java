package com.flinksight.backend.service;

import com.flinksight.backend.domain.JobMetric;
import com.flinksight.backend.mapper.JobMetricStructMapper;
import com.flinksight.backend.repository.JobMetricRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobMetricDTO;
import com.flinksight.common.dto.MetricSeriesDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobMetricService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 任务指标业务实现
 * JobMetric Service Impl
 */
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class JobMetricServiceImpl implements JobMetricService {

    private final JobMetricRepository repository;
    private final JobMetricStructMapper jobMetricStructMapper;

    @Override
    public JobMetricDTO createMetric(JobMetricDTO metricDTO) {
        JobMetric entity = jobMetricStructMapper.toEntity(metricDTO);
        entity.setIsDeleted(0);
        JobMetric saved = repository.save(entity);
        return jobMetricStructMapper.toDTO(saved);
    }

    @Override
    public Optional<JobMetricDTO> getMetricById(Long id) {
        return repository.findById(id).map(jobMetricStructMapper::toDTO);
    }

    @Override
    public PageResult<JobMetricDTO> getMetricsByJob(Long jobId, LocalDateTime start, LocalDateTime end,int page, int size) {
        Page<JobMetric> result = repository.findByJobIdAndMetricTimeBetweenAndIsDeleted(jobId, start, end,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<JobMetricDTO> dtoPage = result.map(jobMetricStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<JobMetricDTO> getMetricsByTenantAndMetric(Long tenantId, String metricKey, LocalDateTime start, LocalDateTime end,int page, int size) {
        Page<JobMetric> result = repository.findByTenantIdAndMetricKeyAndMetricTimeBetweenAndIsDeleted(tenantId, metricKey, start, end,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<JobMetricDTO> dtoPage = result.map(jobMetricStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public MetricSeriesDTO getSeries(Long tenantId, String metricKey, LocalDateTime from, LocalDateTime to) {
        var list = repository.findSeries(tenantId, metricKey, from, to);
        var times  = list.stream().map(v -> v.getTs().toString()).toList();
        var values = list.stream().map(JobMetricDTO::getValue).toList();
        return new MetricSeriesDTO(times, values);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<JobMetricDTO> opt = repository.findById(id).map(jobMetricStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            JobMetricDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(jobMetricStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
