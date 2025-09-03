package com.flinksight.common.service;

import com.flinksight.common.dto.AlertRuleItemDTO;
import com.flinksight.common.dto.RuleSnapshotDTO;
import com.flinksight.common.enums.ScopeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlertRuleQueryService {
  RuleSnapshotDTO snapshot(ScopeType scopeType, Long scopeId);

  Page<AlertRuleItemDTO> list(Long ruleSetId, Integer enabled, Pageable pageable);

  AlertRuleItemDTO get(Long ruleSetId, Long itemId);
}
