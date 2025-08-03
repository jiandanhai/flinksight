package com.flinksight.backend.repository;

import com.flinksight.backend.domain.ApiKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Page<ApiKey> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<ApiKey> findByIsDeleted(Integer isDeleted, Pageable pageable);
}
