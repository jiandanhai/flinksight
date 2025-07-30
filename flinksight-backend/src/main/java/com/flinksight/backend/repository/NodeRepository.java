package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    List<Node> findByClusterIdAndIsDeleted(Long clusterId, Integer isDeleted);
    List<Node> findByIsDeleted(Integer isDeleted);
}
