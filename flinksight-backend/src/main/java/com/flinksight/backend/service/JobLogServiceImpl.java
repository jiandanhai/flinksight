package com.flinksight.backend.service;

import com.flinksight.backend.domain.JobInstance;
import com.flinksight.backend.domain.JobLog;
import com.flinksight.backend.mapper.JobInstanceStructMapper;
import com.flinksight.backend.mapper.JobLogStructMapper;
import com.flinksight.backend.repository.JobLogRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobInstanceDTO;
import com.flinksight.common.dto.JobLogDTO;
import com.flinksight.common.service.JobLogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    private final JobLogStructMapper mapper;

    @Override
    public JobLogDTO createJobLog(JobLogDTO jobLogDTO) {
        JobLog entity = mapper.toEntity(jobLogDTO);
        entity.setIsDeleted(0);
        JobLog saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<JobLogDTO> getJobLogById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<JobLogDTO> getLogsByJob(Long jobId, LocalDateTime start, LocalDateTime end) {
        return mapper.toDTOList(repository.findByJobIdAndTsBetweenAndIsDeleted(jobId, start, end, 0));
    }

    @Override
    public List<JobLogDTO> getLogsByTenantAndLevel(Long tenantId, String level, LocalDateTime start, LocalDateTime end) {
        return mapper.toDTOList(repository.findByTenantIdAndLevelAndTsBetweenAndIsDeleted(tenantId, level, start, end, 0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<JobLogDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            JobLogDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
