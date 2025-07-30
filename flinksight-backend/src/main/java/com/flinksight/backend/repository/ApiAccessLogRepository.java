package com.flinksight.backend.repository;

import com.flinksight.backend.domain.ApiAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiAccessLogRepository extends JpaRepository<ApiAccessLog, Long> {
    List<ApiAccessLog> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}