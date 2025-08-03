package com.flinksight.backend.repository;

import com.flinksight.backend.domain.ApiAccessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiAccessLogRepository extends JpaRepository<ApiAccessLog, Long> {
    Page<ApiAccessLog> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<ApiAccessLog> findByIsDeleted(Integer isDeleted, Pageable pageable);
}