package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.Job;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.JobStructMapper;
import com.flinksight.backend.repository.JobRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobBatchUpdateStatusRequestDTO;
import com.flinksight.common.dto.JobDTO;
import com.flinksight.common.enums.ErrorCode;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 任务业务实现
 * Job Service Impl
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class JobServiceImpl implements JobService {

    private final JobRepository repository;
    private final JobStructMapper jobStructMapper;

    @Override
    public JobDTO createJob(JobDTO jobDTO) {
        Job entity = jobStructMapper.toEntity(jobDTO);
        entity.setIsDeleted(0);
        Job saved = repository.save(entity);
        return jobStructMapper.toDTO(saved);
    }

    @Override
    public Optional<JobDTO> getJobById(Long jobId) {
        return repository.findById(jobId).map(jobStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<JobDTO> list(Long clusterId,int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, Job.class); // 统一 1→0
        Page<Job> result = (clusterId == null || clusterId <= 0)
                ? repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentUserId(), 0, pr)
                : repository.findAllByTenantIdAndClusterIdAndIsDeleted(SecurityUtil.getCurrentTenantId(), clusterId,0, pr);
        return PageHelpers.toPageResult(result, jobStructMapper::toDTO, true); // 返回
    }

    @Override
    public JobDTO updateJob(JobDTO job) {
        Optional<JobDTO> opt = repository.findById(job.getId()).map(jobStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if(opt.isPresent()) {
            JobDTO j = opt.get();
            j.setName(job.getName());
            j.setType(job.getType());
            j.setStatus(job.getStatus());
            j.setStartTime(job.getStartTime());
            j.setEndTime(job.getEndTime());
            // 其它业务字段...
            Job entity = jobStructMapper.toEntity(j);
            entity.setIsDeleted(0);
            Job saved = repository.save(entity);
            return jobStructMapper.toDTO(saved);
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "任务不存在");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateJobStatus(JobBatchUpdateStatusRequestDTO req) {
        log.info("[Job] batchUpdateJobStatus tenantId={}, status={}, ids={}",
                req.getTenantId(), req.getStatus(), req.getJobIds());

        int updated = repository.batchUpdateStatus(req.getTenantId(), req.getJobIds(), req.getStatus());
        log.info("[Job] batchUpdateJobStatus DONE updated={}", updated);
        return updated;
    }

    @Override
    public boolean sDelete(Long jobId) {
        Optional<JobDTO> opt = repository.findById(jobId).map(jobStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            JobDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(jobStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
