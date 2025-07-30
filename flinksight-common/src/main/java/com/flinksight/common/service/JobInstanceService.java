package com.flinksight.common.service;

import com.flinksight.common.dto.JobInstanceDTO;
import com.flinksight.common.dto.JobMetricDTO;

import java.util.List;
import java.util.Optional;

public interface JobInstanceService  extends SoftDeleteService<JobInstanceDTO, Long> {
    JobInstanceDTO createJob(JobInstanceDTO job);

    boolean updateJobStatus(Long id, String status);

    List<JobInstanceDTO> listByTenant(Long tenantId);

    List<JobInstanceDTO> listByStatus(String status);

    Optional<JobInstanceDTO> getById(Long id);

    List<JobInstanceDTO> findByTenantIdAndIsDeleted(Long tenantId);

}
