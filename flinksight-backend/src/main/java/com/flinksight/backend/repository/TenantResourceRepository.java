package com.flinksight.backend.repository;

import com.flinksight.backend.domain.TenantResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TenantResourceRepository extends JpaRepository<TenantResource, Long> {
    List<TenantResource> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
    List<TenantResource> findByResourceIdAndIsDeleted(Long resourceId, Integer isDeleted);
}
