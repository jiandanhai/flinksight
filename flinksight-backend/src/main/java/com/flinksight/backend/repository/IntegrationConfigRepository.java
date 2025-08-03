package com.flinksight.backend.repository;

import com.flinksight.backend.domain.IntegrationConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegrationConfigRepository extends JpaRepository<IntegrationConfig, Long> {
    Page<IntegrationConfig> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<IntegrationConfig> findByIsDeleted(Integer isDeleted, Pageable pageable);
}
