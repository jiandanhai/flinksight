package com.flinksight.backend.repository;

import com.flinksight.backend.domain.OperationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OperationTemplateRepository extends JpaRepository<OperationTemplate, Long> {
    List<OperationTemplate> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
