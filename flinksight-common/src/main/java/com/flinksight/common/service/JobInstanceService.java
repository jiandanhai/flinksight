package com.flinksight.common.service;

import com.flinksight.common.dto.JobInstanceDTO;
import com.flinksight.common.model.PageResult;

import java.util.Map;
import java.util.Optional;

public interface JobInstanceService  extends SoftDeleteService<JobInstanceDTO, Long> {
    JobInstanceDTO createJob(JobInstanceDTO job);

    boolean updateJobStatus(Long id, Integer status);

    PageResult<JobInstanceDTO> listByTenant(Long tenantId,int page, int size);

    PageResult<JobInstanceDTO> listByStatus(Integer status,int page, int size);

    Optional<JobInstanceDTO> getById(Long id);

    PageResult<JobInstanceDTO> findByTenantIdAndIsDeleted(Long tenantId, int page, int size);

    Map<Integer, Long> countStatusByTenantId(Long tenantId);

}
