package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.JobLog;
import com.flinksight.backend.mapper.JobLogStructMapper;
import com.flinksight.backend.repository.JobLogRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.JobLogDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobLogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public PageResult<JobLogDTO> list(Long jobId, String level, LocalDateTime start, LocalDateTime end, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, JobLog.class); // 统一 1→0
        Page<JobLog> result = repository.pageQuery(SecurityUtil.getCurrentTenantId(), jobId, level, start, end, pr);
        return PageHelpers.toPageResult(result, jobLogStructMapper::toDTO, true); // 返回 1
    }

    @Override
    public boolean sDelete(Long id) {
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
