package com.flinksight.backend.repository;

import com.flinksight.backend.domain.ResourceLabel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceLabelRepository extends JpaRepository<ResourceLabel, Long> {
    Page<ResourceLabel> findByResourceIdAndIsDeleted(Long resourceId, Integer isDeleted, Pageable pageable);
    Page<ResourceLabel> findByLabelIdAndIsDeleted(Long labelId, Integer isDeleted, Pageable pageable);
    List<ResourceLabel> findByResourceIdAndLabelIdAndIsDeleted(Long resourceId, Long labelId, Integer isDeleted);
}
