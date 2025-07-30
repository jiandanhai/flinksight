package com.flinksight.backend.repository;

import com.flinksight.backend.domain.IntegrationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IntegrationConfigRepository extends JpaRepository<IntegrationConfig, Long> {
    List<IntegrationConfig> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
