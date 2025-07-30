package com.flinksight.backend.security;

import com.flinksight.backend.domain.NodeHealth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NodeHealthRepository extends JpaRepository<NodeHealth, Long> {
    List<NodeHealth> findByNodeIdAndIsDeleted(Long nodeId, Integer isDeleted);
    List<NodeHealth> findByIsDeleted(Integer isDeleted);
}
