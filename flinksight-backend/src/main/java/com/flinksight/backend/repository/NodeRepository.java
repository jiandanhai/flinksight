package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    Page<Node> findByClusterIdAndIsDeleted(Long clusterId, Integer isDeleted, Pageable pageable);

    Page<Node> findAllByClusterId(Long clusterId,Pageable pageable);

    List<Node> findAllByIdInAndIsDeleted(Collection<Long> ids, Integer isDeleted);

    Optional<Node> findByIdAndIsDeleted(Long id, Integer isDeleted);

    boolean existsByIpAndIsDeleted(String ip, Integer isDeleted);
}
