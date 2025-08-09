package com.flinksight.backend.repository;

import com.flinksight.backend.domain.AlertRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 报警规则表数据访问接口
 * AlertRule Repository
 */
@Repository
public interface AlertRuleRepository extends JpaRepository<AlertRule, Long>, SoftDeleteRepository<AlertRule, Long>  {

    Page<AlertRule> findByTenantIdAndEnableAndIsDeleted(Long tenantId, Integer enable, Integer isDeleted, Pageable pageable);

    // 可根据业务补充：如按clusterId等多条件查询
    Page<AlertRule> findByTenantIdAndClusterIdAndIsDeleted(Long tenantId, Long clusterId, Integer isDeleted, Pageable pageable);

}
