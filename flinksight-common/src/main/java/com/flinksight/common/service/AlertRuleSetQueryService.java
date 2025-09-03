// src/main/java/com/flinksight/backend/service/AlertRuleSetQueryService.java
package com.flinksight.common.service;

import com.flinksight.common.dto.RuleSetDetailDTO;
import com.flinksight.common.dto.RuleSetSummaryDTO;
import com.flinksight.common.enums.RuleSetStatus;
import com.flinksight.common.enums.ScopeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlertRuleSetQueryService {
  Page<RuleSetSummaryDTO> listVersions(Long tenantId, ScopeType scopeType, Long scopeId,
                                       RuleSetStatus status, Integer activeFlag,
                                       Pageable pageable);

  RuleSetDetailDTO detail(Long ruleSetId);
}
