package com.flinksight.common.service;

import com.flinksight.common.dto.AlertRuleDTO;

import java.util.List;
import java.util.Optional;

/**
 * 报警规则业务接口
 * AlertRule Service
 */
public interface AlertRuleService  extends SoftDeleteService<AlertRuleDTO, Long>{
    AlertRuleDTO createRule(AlertRuleDTO rule);
    Optional<AlertRuleDTO> getRuleById(Long id);
    List<AlertRuleDTO> getRulesByTenant(Long tenantId);
    AlertRuleDTO updateRule(AlertRuleDTO rule);
}
