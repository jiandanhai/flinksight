package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobInstanceRepository extends JpaRepository<JobInstance, Long> {
    Page<JobInstance> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);

    Page<JobInstance> findByStatusAndIsDeleted(Integer status, Integer isDeleted, Pageable pageable);

    Page<JobInstance> findByEngineTypeAndIsDeleted(String engineType, Integer isDeleted, Pageable pageable);

    @Query("SELECT j.status, COUNT(j.id) FROM JobInstance j WHERE j.tenantId = :tenantId AND j.isDeleted = 0 GROUP BY j.status")
    List<Object[]> countStatusByTenantId(@Param("tenantId") Long tenantId);

    List<JobInstance> findByTenantIdAndCreatedAtBetween(Long tenantId, LocalDateTime start, LocalDateTime end);

    long countByTenantIdAndStatusAndCreatedAtBetween(Long tenantId, Integer status, LocalDateTime start, LocalDateTime end);

    long countByTenantIdAndStatus(Long tenantId, Integer status);

}
