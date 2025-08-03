package com.flinksight.backend.repository;

import com.flinksight.backend.domain.DataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {
    Page<DataSource> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<DataSource> findByIsDeleted(Integer isDeleted, Pageable pageable);
}
