package com.flinksight.backend.repository;

import com.flinksight.backend.domain.JobAlertRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 作业报警规则仓库
 */
@Repository
public interface JobAlertRuleRepository extends JpaRepository<JobAlertRule, Long> {

    /**
     * 查询某租户的全部有效报警规则
     */
    Page<JobAlertRule> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);

    /**
     * 查询某作业类型下的报警规则
     */
    Page<JobAlertRule> findByJobTypeAndIsDeleted(String jobType, Integer isDeleted, Pageable pageable);

    /**
     * 查询指定规则名（去重防止重复）
     */
    Page<JobAlertRule> findByRuleNameAndIsDeleted(String ruleName, Integer isDeleted, Pageable pageable);

    /**
     * 查询所有未删除规则
     */
    Page<JobAlertRule> findByIsDeleted(Integer isDeleted, Pageable pageable);
}
