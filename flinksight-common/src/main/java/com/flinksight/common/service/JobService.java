package com.flinksight.common.service;

import com.flinksight.common.dto.JobDTO;

import java.util.List;
import java.util.Optional;

/**
 * 任务业务接口
 * Job Service
 */
public interface JobService extends SoftDeleteService<JobDTO, Long>   {
    JobDTO createJob(JobDTO job);
    Optional<JobDTO> getJobById(Long jobId);
    List<JobDTO> getJobsByTenant(Long tenantId);
    List<JobDTO> getJobsByTenantAndCluster(Long tenantId, Long clusterId);
    JobDTO updateJob(JobDTO job);
}
