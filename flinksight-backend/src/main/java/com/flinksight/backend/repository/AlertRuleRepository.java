package com.flinksight.backend.repository;

import com.flinksight.backend.domain.AlertRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 报警规则表数据访问接口
 * AlertRule Repository
 */
@Repository
public interface AlertRuleRepository extends JpaRepository<AlertRule, Long>, SoftDeleteRepository<AlertRule, Long>  {
    // 可根据业务补充：如按clusterId等多条件查询
    List<AlertRule> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);

    Optional<AlertRule> findByIdAndTenantIdAndIsDeleted(Long id, Long tenantId, Integer isDeleted);


    Page<AlertRule> findAllByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);

    Page<AlertRule> findAllByTenantIdAndClusterIdAndIsDeleted(Long tenantId, Long clusterId, Integer isDeleted, Pageable pageable);

    Page<AlertRule> findAllByTenantIdAndEnableAndIsDeleted(Long tenantId, Integer enable, Integer isDeleted, Pageable pageable);

    Page<AlertRule> findAllByTenantIdAndClusterIdAndEnableAndIsDeleted(Long tenantId, Long clusterId, Integer enable, Integer isDeleted, Pageable pageable);


    @Query("""
  SELECT r FROM AlertRule r
   WHERE r.isDeleted = 0
     AND r.tenantId = :tenantId
     AND (:clusterId IS NULL OR r.clusterId = :clusterId)
     AND (:enable   IS NULL OR r.enable    = :enable)
     AND (:kw IS NULL OR r.name LIKE CONCAT('%', :kw, '%') OR r.metricKey LIKE CONCAT('%', :kw, '%'))
""")
    Page<AlertRule> pageQuery(@Param("tenantId") Long tenantId,
                              @Param("clusterId") Long clusterId,
                              @Param("enable") Integer enable,
                              @Param("kw") String keyword,
                              Pageable pageable);

}
