package com.flinksight.backend.service;

import com.flinksight.backend.domain.Job;
import com.flinksight.backend.domain.JobMetric;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.JobMetricStructMapper;
import com.flinksight.backend.mapper.JobStructMapper;
import com.flinksight.backend.repository.JobRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobDTO;
import com.flinksight.common.dto.JobMetricDTO;
import com.flinksight.common.service.JobService;
import com.flinksight.common.enums.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 任务业务实现
 * Job Service Impl
 */
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class JobServiceImpl implements JobService {

    private final JobRepository repository;
    private final JobStructMapper mapper;

    @Override
    public JobDTO createJob(JobDTO jobDTO) {
        Job entity = mapper.toEntity(jobDTO);
        entity.setIsDeleted(0);
        Job saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<JobDTO> getJobById(Long jobId) {
        return repository.findById(jobId).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<JobDTO> getJobsByTenant(Long tenantId) {
        return mapper.toDTOList(repository.findAllByTenantIdAndIsDeleted(tenantId, 0));
    }

    @Override
    public List<JobDTO> getJobsByTenantAndCluster(Long tenantId, Long clusterId) {
        return mapper.toDTOList(repository.findAllByTenantIdAndClusterIdAndIsDeleted(tenantId, clusterId, 0));
    }

    @Override
    public JobDTO updateJob(JobDTO job) {
        Optional<JobDTO> opt = repository.findById(job.getId()).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if(opt.isPresent()) {
            JobDTO j = opt.get();
            j.setName(job.getName());
            j.setType(job.getType());
            j.setStatus(job.getStatus());
            j.setStartTime(job.getStartTime());
            j.setEndTime(job.getEndTime());
            // 其它业务字段...
            Job entity = mapper.toEntity(j);
            entity.setIsDeleted(0);
            Job saved = repository.save(entity);
            return mapper.toDTO(saved);
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "任务不存在");
    }

    @Override
    public boolean softDelete(Long jobId) {
        Optional<JobDTO> opt = repository.findById(jobId).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            JobDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
