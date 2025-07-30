package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
