package com.flinksight.backend.repository;

import com.flinksight.backend.domain.OperationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationTemplateRepository extends JpaRepository<OperationTemplate, Long> {
    Page<OperationTemplate> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<OperationTemplate> findByIsDeleted(Integer isDeleted, Pageable pageable);
}
