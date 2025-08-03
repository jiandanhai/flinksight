package com.flinksight.backend.service;

import com.flinksight.backend.domain.JobLog;
import com.flinksight.backend.mapper.JobLogStructMapper;
import com.flinksight.backend.repository.JobLogRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobLogDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobLogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 任务日志业务实现
 * JobLog Service Impl
 */
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class JobLogServiceImpl implements JobLogService {

    private final JobLogRepository repository;
    private final JobLogStructMapper jobLogStructMapper;

    @Override
    public JobLogDTO createJobLog(JobLogDTO jobLogDTO) {
        JobLog entity = jobLogStructMapper.toEntity(jobLogDTO);
        entity.setIsDeleted(0);
        JobLog saved = repository.save(entity);
        return jobLogStructMapper.toDTO(saved);
    }

    @Override
    public Optional<JobLogDTO> getJobLogById(Long id) {
        return repository.findById(id).map(jobLogStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<JobLogDTO> getLogsByJob(Long jobId, LocalDateTime start, LocalDateTime end,int page, int size) {
        Page<JobLog> result = repository.findByJobIdAndLogTimeBetweenAndIsDeleted(jobId, start, end,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<JobLogDTO> dtoPage = result.map(jobLogStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<JobLogDTO> getLogsByTenantAndLevel(Long tenantId, String level, LocalDateTime start, LocalDateTime end,int page, int size) {
        Page<JobLog> result = repository.findByTenantIdAndLevelAndLogTimeBetweenAndIsDeleted(tenantId, level, start, end,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<JobLogDTO> dtoPage = result.map(jobLogStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<JobLogDTO> opt = repository.findById(id).map(jobLogStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            JobLogDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(jobLogStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
