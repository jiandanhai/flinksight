package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.JobDiagnosticLog;
import com.flinksight.backend.mapper.JobDiagnosticLogStructMapper;
import com.flinksight.backend.repository.JobDiagnosticLogRepository;
import com.flinksight.common.dto.JobDiagnosticLogDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobDiagnosticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JobDiagnosticServiceImpl implements JobDiagnosticService {

    private final JobDiagnosticLogRepository jobDiagnosticLogRepository;
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
        jobDiagnosticLogRepository.save(log);
    }

    @Override
    public PageResult<JobDiagnosticLogDTO> list(Long jobId,String level,int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, JobDiagnosticLog.class); // 统一 1→0
        Page<JobDiagnosticLog> result = jobDiagnosticLogRepository.pageQuery(jobId, level, pr);
        return PageHelpers.toPageResult(result, jobDiagnosticLogStructMapper::toDTO, true); // 返回 1-b
    }
}
