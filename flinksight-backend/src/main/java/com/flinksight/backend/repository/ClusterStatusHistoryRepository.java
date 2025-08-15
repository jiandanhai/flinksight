package com.flinksight.backend.repository;

import com.flinksight.backend.domain.ClusterStatusHistory;
import com.flinksight.backend.repository.projection.ClusterStatusTrendProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClusterStatusHistoryRepository extends JpaRepository<ClusterStatusHistory, Long> {
    Page<ClusterStatusHistory> findByClusterIdAndIsDeletedOrderByCollectTimeDesc(Long clusterId, Integer isDeleted, Pageable pageable);

    List<ClusterStatusHistory> findByCollectTimeBetweenAndIsDeleted(
            LocalDateTime from, LocalDateTime to, Integer isDeleted
    );

    @Query("""
        SELECT 
            FUNCTION('DATE_FORMAT', h.collectTime, '%Y-%m-%d') AS day,
            c.status AS status,
            COUNT(h.id) AS cnt
        FROM ClusterStatusHistory h
        JOIN Cluster c ON h.clusterId = c.id
        WHERE c.tenantId = :tenantId
          AND h.collectTime >= :from
          AND h.collectTime < :to
          AND h.isDeleted = 0
        GROUP BY FUNCTION('DATE_FORMAT', h.collectTime, '%Y-%m-%d'), c.status
        ORDER BY day ASC
    """)
    List<ClusterStatusTrendProjection> findClusterStatusTrend(
            @Param("tenantId") Long tenantId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
