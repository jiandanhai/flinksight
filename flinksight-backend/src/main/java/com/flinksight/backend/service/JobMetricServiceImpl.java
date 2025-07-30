package com.flinksight.backend.service;

import com.flinksight.backend.domain.JobMetric;
import com.flinksight.backend.mapper.JobMetricStructMapper;
import com.flinksight.backend.repository.JobMetricRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobMetricDTO;
import com.flinksight.common.service.JobMetricService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    private final JobMetricStructMapper mapper;

    @Override
    public JobMetricDTO createMetric(JobMetricDTO metricDTO) {
        JobMetric entity = mapper.toEntity(metricDTO);
        entity.setIsDeleted(0);
        JobMetric saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<JobMetricDTO> getMetricById(Long id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public List<JobMetricDTO> getMetricsByJob(Long jobId, LocalDateTime start, LocalDateTime end) {
        return mapper.toDTOList(repository.findByJobIdAndTsBetweenAndIsDeleted(jobId, start, end, 0));
    }

    @Override
    public List<JobMetricDTO> getMetricsByTenantAndMetric(Long tenantId, String metricKey, LocalDateTime start, LocalDateTime end) {
        return mapper.toDTOList(repository.findByTenantIdAndMetricKeyAndTsBetweenAndIsDeleted(tenantId, metricKey, start, end, 0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<JobMetricDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            JobMetricDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
