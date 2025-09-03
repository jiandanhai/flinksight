// package com.flinksight.backend.service
package com.flinksight.common.service;

import com.flinksight.common.dto.*;
import com.flinksight.common.enums.ScopeType;

import java.util.List;

public interface AlertRuleAdminService {
  Long createDraft(ScopeType scopeType, Long scopeId, Long version, String operator);
  void replaceItems(Long ruleSetId, List<AlertRuleItemDTO> items);        // 幂等替换
  void activate(Long ruleSetId);

  AlertRuleItemDTO update(Long ruleSetId, Long itemId, AlertRuleItemUpdateRequestDTO req);

  void delete(Long ruleSetId, Long itemId);

  int deleteBatch(Long ruleSetId, IdListRequestDTO ids);

  int enableBatch(Long ruleSetId, BatchEnableRequestDTO req);
  // 原子激活（同 scope 仅一条 ACTIVE）
}
