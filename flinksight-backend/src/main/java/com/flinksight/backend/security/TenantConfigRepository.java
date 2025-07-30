package com.flinksight.backend.security;

import com.flinksight.backend.domain.TenantConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TenantConfigRepository extends JpaRepository<TenantConfig, Long> {
    List<TenantConfig> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
