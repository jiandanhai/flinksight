package com.flinksight.backend.repository;

import com.flinksight.backend.domain.ClusterStatusHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClusterStatusHistoryRepository extends JpaRepository<ClusterStatusHistory, Long> {
    Page<ClusterStatusHistory> findByClusterIdAndIsDeletedOrderByCollectTimeDesc(Long clusterId, Integer isDeleted, Pageable pageable);
}
