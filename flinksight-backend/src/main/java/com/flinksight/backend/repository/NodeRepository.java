package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    Page<Node> findByClusterIdAndIsDeleted(Long clusterId, Integer isDeleted, Pageable pageable);
    Page<Node> findByIsDeleted(Integer isDeleted, Pageable pageable);
}
