package com.flinksight.backend.repository;

import com.flinksight.backend.domain.ClusterStatusHistory;
import com.flinksight.common.dto.ClusterStatusHistoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClusterStatusHistoryRepository extends JpaRepository<ClusterStatusHistory, Long> {
    List<ClusterStatusHistory> findTopNByClusterIdAndIsDeletedOrderByCollectTimeDesc(Long clusterId, Integer isDeleted, int limit);
}
