package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    Page<Label> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
}
