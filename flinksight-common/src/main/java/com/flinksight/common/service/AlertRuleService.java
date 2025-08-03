package com.flinksight.common.service;

import com.flinksight.common.dto.AlertRuleDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

/**
 * 报警规则业务接口
 * AlertRule Service
 */
public interface AlertRuleService  extends SoftDeleteService<AlertRuleDTO, Long>{
    AlertRuleDTO createRule(AlertRuleDTO rule);
    Optional<AlertRuleDTO> getRuleById(Long id);
    PageResult<AlertRuleDTO> getRulesByTenant(Long tenantId,int page, int size);
    AlertRuleDTO updateRule(AlertRuleDTO rule);
}
