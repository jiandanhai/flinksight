package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Node;
import com.flinksight.common.service.projection.NodeListRow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    /** 分页 + 统一回填最新健康（只认这一个口径） */
    @Query(value = """
        SELECT 
          n.id               AS id,
          n.tenant_id        AS tenantId,
          n.cluster_id       AS clusterId,
          n.name             AS name,
          n.type             AS type,
          n.ip               AS ip,
          n.status           AS status,
          n.create_time      AS createTime,
          lh.health_status   AS health,
          lh.message         AS healthMessage,
          lh.check_time      AS healthTime
        FROM node n
        LEFT JOIN node_health lh 
          ON lh.node_id = n.id
         AND lh.check_time = (
            SELECT MAX(h2.check_time)
            FROM node_health h2
            WHERE h2.tenant_id = :tenantId
              AND h2.node_id   = n.id
              AND h2.is_deleted= 0
         )
        WHERE n.tenant_id = :tenantId
          AND n.cluster_id = :clusterId
          AND n.is_deleted = 0
          AND (:kw IS NULL OR :kw = '' OR n.name LIKE CONCAT('%',:kw,'%') OR n.ip LIKE CONCAT('%',:kw,'%'))
        ORDER BY n.id DESC
        """,
            countQuery = """
        SELECT COUNT(1)
        FROM node n
        WHERE n.tenant_id = :tenantId
          AND n.cluster_id = :clusterId
          AND n.is_deleted = 0
          AND (:kw IS NULL OR :kw = '' OR n.name LIKE CONCAT('%',:kw,'%') OR n.ip LIKE CONCAT('%',:kw,'%'))
        """,
            nativeQuery = true)
    Page<NodeListRow> pageWithLatestHealth(@Param("tenantId") Long tenantId,
                                           @Param("clusterId") Long clusterId,
                                           @Param("kw") String keyword,
                                           Pageable pageable);

    /** 健康桶（与列表口径完全一致） */
    @Query(value = """
        SELECT COALESCE(lh.health_status, 'UNKNOWN') AS health, COUNT(*) AS cnt
        FROM node n
        LEFT JOIN node_health lh 
          ON lh.node_id = n.id
         AND lh.check_time = (
            SELECT MAX(h2.check_time)
            FROM node_health h2
            WHERE h2.tenant_id = :tenantId
              AND h2.node_id   = n.id
              AND h2.is_deleted= 0
         )
        WHERE n.tenant_id = :tenantId
          AND n.cluster_id = :clusterId
          AND n.is_deleted = 0
        GROUP BY COALESCE(lh.health_status, 'UNKNOWN')
        """,
            nativeQuery = true)
    List<Object[]> healthBuckets(@Param("tenantId") Long tenantId,
                                 @Param("clusterId") Long clusterId);
}
