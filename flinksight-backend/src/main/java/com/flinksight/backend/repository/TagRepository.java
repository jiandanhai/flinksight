package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
