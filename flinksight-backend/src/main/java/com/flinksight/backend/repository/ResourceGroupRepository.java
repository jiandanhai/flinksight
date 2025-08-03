package com.flinksight.backend.repository;

import com.flinksight.backend.domain.ResourceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceGroupRepository extends JpaRepository<ResourceGroup, Long> {
    Page<ResourceGroup> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<ResourceGroup> findByIsDeleted(Integer isDeleted, Pageable pageable);
}
