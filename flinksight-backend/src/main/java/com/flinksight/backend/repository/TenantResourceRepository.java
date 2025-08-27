package com.flinksight.backend.repository;

import com.flinksight.backend.domain.TenantResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantResourceRepository extends JpaRepository<TenantResource, Long> {
    List<TenantResource> findByTenantIdAndResourceIdAndIsDeleted(Long tenantId,Long resourceId, Integer isDeleted);
    Page<TenantResource> findByTenantIdAndResourceIdAndIsDeleted(Long tenantId,Long resourceId, Integer isDeleted, Pageable pageable);
    Page<TenantResource> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted,Pageable pageable);

    @Query("""
    SELECT tr FROM TenantResource tr
     WHERE tr.tenantId = :tenantId
       AND tr.isDeleted = 0
       AND (:resourceId IS NULL OR tr.resourceId = :resourceId)
  """)
    Page<TenantResource> pageQuery(@Param("tenantId") Long tenantId,
                                   @Param("resourceId") Long resourceId,
                                   Pageable pageable);
}
