package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.JobMetric;
import com.flinksight.backend.mapper.JobMetricStructMapper;
import com.flinksight.backend.repository.JobMetricRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobMetricDTO;
import com.flinksight.common.dto.MetricSeriesDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobMetricService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public PageResult<JobMetricDTO> list(Long jobId, String metricKey, LocalDateTime start, LocalDateTime end, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, JobMetric.class); // 统一 1→0
        Page<JobMetric> result = repository.pageQuery(SecurityUtil.getCurrentTenantId(), jobId, metricKey, start, end, pr);
        return PageHelpers.toPageResult(result, jobMetricStructMapper::toDTO, true); // 返回 1
    }

    @Override
    public MetricSeriesDTO getSeries(String metricKey, LocalDateTime from, LocalDateTime to) {
        var list = repository.findSeries(SecurityUtil.getCurrentTenantId(), metricKey, from, to);
        var times  = list.stream().map(v -> v.getTs().toString()).toList();
        var values = list.stream().map(JobMetricDTO::getValue).toList();
        return new MetricSeriesDTO(times, values);
    }

    @Override
    public boolean sDelete(Long id) {
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
