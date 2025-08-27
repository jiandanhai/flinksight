package com.flinksight.common.service;

import com.flinksight.common.dto.JobDiagnosticLogDTO;
import com.flinksight.common.model.PageResult;

public interface JobDiagnosticService {
    void log(Long jobId, String jobName, String level, String content, String traceId);

    PageResult<JobDiagnosticLogDTO> list(Long jobId,String level,int page, int size);

}
