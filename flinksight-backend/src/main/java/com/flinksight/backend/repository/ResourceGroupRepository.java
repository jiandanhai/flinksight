package com.flinksight.backend.repository;

import com.flinksight.backend.domain.ResourceGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResourceGroupRepository extends JpaRepository<ResourceGroup, Long> {
    List<ResourceGroup> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
