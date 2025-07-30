package com.flinksight.backend.repository;

import com.flinksight.backend.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
