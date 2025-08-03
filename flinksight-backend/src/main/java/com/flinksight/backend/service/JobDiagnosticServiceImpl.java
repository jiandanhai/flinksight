package com.flinksight.backend.service;

import com.flinksight.backend.domain.JobDiagnosticLog;
import com.flinksight.backend.mapper.JobDiagnosticLogStructMapper;
import com.flinksight.backend.repository.JobDiagnosticLogRepository;
import com.flinksight.common.dto.JobDiagnosticLogDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobDiagnosticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JobDiagnosticServiceImpl implements JobDiagnosticService {

    private final JobDiagnosticLogRepository repo;
    private final JobDiagnosticLogStructMapper jobDiagnosticLogStructMapper;

    @Override
    public void log(Long jobId, String jobName, String level, String content, String traceId) {
        JobDiagnosticLog log = JobDiagnosticLog.builder()
                .jobId(jobId)
                .jobName(jobName)
                .logTime(LocalDateTime.now())
                .level(level)
                .content(content)
                .traceId(traceId)
                .isDeleted(0)
                .build();
        repo.save(log);
    }

    @Override
    public PageResult<JobDiagnosticLogDTO> getLogsByJob(Long jobId,int page, int size) {
        Page<JobDiagnosticLog> result = repo.findByJobIdAndIsDeleted(jobId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<JobDiagnosticLogDTO> dtoPage = result.map(jobDiagnosticLogStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<JobDiagnosticLogDTO> getLogsByLevel(String level,int page, int size) {
        Page<JobDiagnosticLog> result = repo.findByLevelAndIsDeleted(level,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<JobDiagnosticLogDTO> dtoPage = result.map(jobDiagnosticLogStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }
}
