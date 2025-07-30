package com.flinksight.common.service;

import com.flinksight.common.dto.JobLogDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 任务日志业务接口
 * JobLog Service
 */
public interface JobLogService extends SoftDeleteService<JobLogDTO, Long>   {
    JobLogDTO createJobLog(JobLogDTO log);
    Optional<JobLogDTO> getJobLogById(Long id);
    List<JobLogDTO> getLogsByJob(Long jobId, LocalDateTime start, LocalDateTime end);
    List<JobLogDTO> getLogsByTenantAndLevel(Long tenantId, String level, LocalDateTime start, LocalDateTime end);
}
