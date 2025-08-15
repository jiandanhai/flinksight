package com.flinksight.backend.repository;

import com.flinksight.backend.domain.AlertHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {
    Page<AlertHistory> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);

    Page<AlertHistory> findByIsDeleted(Integer isDeleted, Pageable pageable);

    List<AlertHistory> findByTenantId(Long tenantId);

    @Query("SELECT a.level AS level, COUNT(a.id) AS count FROM AlertHistory a WHERE a.tenantId = :tenantId AND a.createdAt BETWEEN :start AND :end GROUP BY a.level")
    List<Map<String, Object>> countByLevelBetween(@Param("tenantId") Long tenantId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT FUNCTION('DATE_FORMAT', a.createdAt, '%Y-%m-%d') AS date, COUNT(a.id) AS count FROM AlertHistory a WHERE a.tenantId = :tenantId AND a.createdAt BETWEEN :start AND :end GROUP BY date ORDER BY date")
    List<Map<String, Object>> countTrendByDate(@Param("tenantId") Long tenantId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


    @Query("SELECT a.status AS status, COUNT(a.id) AS count " +
            "FROM AlertHistory a " +
            "WHERE a.tenantId = :tenantId AND a.createdAt BETWEEN :start AND :end " +
            "GROUP BY a.status")
    List<Map<String, Object>> countByStatusBetween(@Param("tenantId") Long tenantId,
                                                   @Param("start") LocalDateTime start,
                                                   @Param("end") LocalDateTime end);

    @Query("SELECT AVG(FUNCTION('TIMESTAMPDIFF', SECOND, a.createdAt, a.operateTime)) AS avgDurationSec " +
            "FROM AlertHistory a " +
            "WHERE a.tenantId = :tenantId AND a.status = 1 AND a.operateTime IS NOT NULL")
    Double averageResponseTimeSeconds(@Param("tenantId") Long tenantId);
}
