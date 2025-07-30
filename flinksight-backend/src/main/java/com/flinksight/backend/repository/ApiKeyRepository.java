package com.flinksight.backend.repository;

import com.flinksight.backend.domain.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    List<ApiKey> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
