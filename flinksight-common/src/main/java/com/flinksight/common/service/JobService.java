package com.flinksight.common.service;

import com.flinksight.common.dto.JobDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

/**
 * 任务业务接口
 * Job Service
 */
public interface JobService extends SoftDeleteService<JobDTO, Long>   {
    JobDTO createJob(JobDTO job);
    Optional<JobDTO> getJobById(Long jobId);
    PageResult<JobDTO> getAll(int page, int size);
    PageResult<JobDTO> getJobsByTenant(Long tenantId,int page, int size);
    PageResult<JobDTO> getJobsByTenantAndCluster(Long tenantId, Long clusterId,int page, int size);
    JobDTO updateJob(JobDTO job);
}
