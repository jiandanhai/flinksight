package com.flinksight.common.service;

import com.flinksight.common.dto.AlertRuleDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

/**
 * 报警规则业务接口
 * AlertRule Service
 */
public interface AlertRuleService  extends SoftDeleteService<AlertRuleDTO, Long>{
    AlertRuleDTO createAlertRule(AlertRuleDTO rule);
    Optional<AlertRuleDTO> getAlertRuleById(Long id);
    PageResult<AlertRuleDTO> getAlertRulesByTenant(Long tenantId, int page, int size);
    /**
     * 按租户与集群查询
     */
    PageResult<AlertRuleDTO> listByTenantAndCluster(Long tenantId, Long clusterId, int page, int size);

    AlertRuleDTO updateAlertRule(AlertRuleDTO rule);
}
