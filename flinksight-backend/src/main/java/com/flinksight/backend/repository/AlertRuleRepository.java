package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Alert;
import com.flinksight.backend.domain.AlertRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报警规则表数据访问接口
 * AlertRule Repository
 */
@Repository
public interface AlertRuleRepository extends JpaRepository<AlertRule, Long>, SoftDeleteRepository<AlertRule, Long>  {

    List<AlertRule> findByTenantIdAndEnableAndIsDeleted(Long tenantId, Integer enable, Integer isDeleted);
}
