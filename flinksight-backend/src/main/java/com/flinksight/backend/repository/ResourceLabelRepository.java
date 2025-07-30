package com.flinksight.backend.repository;

import com.flinksight.backend.domain.ResourceLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResourceLabelRepository extends JpaRepository<ResourceLabel, Long> {
    List<ResourceLabel> findByResourceIdAndIsDeleted(Long resourceId, Integer isDeleted);
    List<ResourceLabel> findByLabelIdAndIsDeleted(Long labelId, Integer isDeleted);
}
