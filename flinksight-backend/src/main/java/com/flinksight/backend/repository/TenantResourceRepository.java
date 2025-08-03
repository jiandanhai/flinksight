package com.flinksight.backend.repository;

import com.flinksight.backend.domain.TenantResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantResourceRepository extends JpaRepository<TenantResource, Long> {
    List<TenantResource> findByTenantIdAndResourceIdAndIsDeleted(Long tenantId,Long resourceId, Integer isDeleted);
    Page<TenantResource> findByResourceIdAndIsDeleted(Long resourceId, Integer isDeleted, Pageable pageable);
    Page<TenantResource> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted,Pageable pageable);
}
