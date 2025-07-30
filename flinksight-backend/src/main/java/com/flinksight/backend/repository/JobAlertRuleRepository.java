package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobAlertRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 作业报警规则仓库
 */
@Repository
public interface JobAlertRuleRepository extends JpaRepository<JobAlertRule, Long> {

    /**
     * 查询某租户的全部有效报警规则
     */
    List<JobAlertRule> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);

    /**
     * 查询某作业类型下的报警规则
     */
    List<JobAlertRule> findByJobTypeAndIsDeleted(String jobType, Integer isDeleted);

    /**
     * 查询指定规则名（去重防止重复）
     */
    List<JobAlertRule> findByRuleNameAndIsDeleted(String ruleName, Integer isDeleted);

    /**
     * 查询所有未删除规则
     */
    List<JobAlertRule> findByIsDeleted(Integer isDeleted);
}
