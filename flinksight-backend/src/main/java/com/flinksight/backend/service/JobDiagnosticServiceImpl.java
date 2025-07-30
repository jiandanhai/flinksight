package com.flinksight.backend.service;

import com.flinksight.backend.domain.JobDiagnosticLog;
import com.flinksight.backend.mapper.IntegrationConfigStructMapper;
import com.flinksight.backend.mapper.JobDiagnosticLogStructMapper;
import com.flinksight.backend.repository.JobDiagnosticLogRepository;
import com.flinksight.common.dto.JobDiagnosticLogDTO;
import com.flinksight.common.service.JobDiagnosticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobDiagnosticServiceImpl implements JobDiagnosticService {

    private final JobDiagnosticLogRepository repo;
    private final JobDiagnosticLogStructMapper mapper;

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
    public List<JobDiagnosticLogDTO> getLogsByJob(Long jobId) {
        return mapper.toDTOList(repo.findByJobIdAndIsDeleted(jobId,0));
    }

    @Override
    public List<JobDiagnosticLogDTO> getLogsByLevel(String level) {
        return mapper.toDTOList(repo.findByLevelAndIsDeleted(level,0));
    }
}
