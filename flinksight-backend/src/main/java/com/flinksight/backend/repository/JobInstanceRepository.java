package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobInstanceRepository extends JpaRepository<JobInstance, Long> {
    List<JobInstance> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);

    List<JobInstance> findByStatusAndIsDeleted(String status, Integer isDeleted);

    List<JobInstance> findByEngineTypeAndIsDeleted(String engineType, Integer isDeleted);
}
