package com.flinksight.common.service;

import com.flinksight.common.dto.JobBatchUpdateStatusRequestDTO;
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
    PageResult<JobDTO> list(Long clusterId,int page, int size);
    JobDTO updateJob(JobDTO job);

    /**
     * 批量更新任务状态（带租户隔离，软删保护）
     * @return 实际更新条数
     */
    int batchUpdateJobStatus(JobBatchUpdateStatusRequestDTO req);
}
