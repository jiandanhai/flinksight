package com.flinksight.common.service;

import com.flinksight.common.dto.JobLogDTO;
import com.flinksight.common.model.PageResult;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 任务日志业务接口
 * JobLog Service
 */
public interface JobLogService extends SoftDeleteService<JobLogDTO, Long>   {
    JobLogDTO createJobLog(JobLogDTO log);
    Optional<JobLogDTO> getJobLogById(Long id);
    PageResult<JobLogDTO> getLogsByJob(Long jobId, LocalDateTime start, LocalDateTime end,int page, int size);
    PageResult<JobLogDTO> getLogsByTenantAndLevel(Long tenantId, String level, LocalDateTime start, LocalDateTime end,int page, int size);
}
