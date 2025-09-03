// src/main/java/com/flinksight/backend/service/AlertRuleSetAdminService.java
package com.flinksight.common.service;

public interface AlertRuleSetAdminService {
  void deleteDraft(Long ruleSetId);
  void archive(Long ruleSetId);   // ACTIVE → ARCHIVED（紧急下线）
  void rollbackTo(Long ruleSetId);// 激活指定历史版本（等价于 activate(ruleSetId)）
}
