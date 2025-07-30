package com.flinksight.backend.repository;

import com.flinksight.backend.domain.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {
    List<DataSource> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
