package com.flinksight.common.service;

import com.flinksight.common.dto.JobDiagnosticLogDTO;

import java.util.List;

public interface JobDiagnosticService {
    void log(Long jobId, String jobName, String level, String content, String traceId);

    List<JobDiagnosticLogDTO> getLogsByJob(Long jobId);

    List<JobDiagnosticLogDTO> getLogsByLevel(String level);
}
