// src/main/java/com/flinksight/backend/events/RuleSetChangeEvent.java
package com.flinksight.backend.events;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class RuleSetChangeEvent {

  public enum Action { DRAFT_CREATED, ITEMS_REPLACED, ACTIVATED, ARCHIVED, ROLLED_BACK }

  private Action action;

  private Long ruleSetId;
  private Long tenantId;

  private String scopeType;  // TENANT/CLUSTER/JOB
  private Long scopeId;      // TENANT 级可为空

  private Long version;      // 当前版本号
  private String checksum;   // 快照指纹（sha256）

  /** 受影响的规则项 ID（可选；ITEMS_REPLACED 时较有用） */
  private List<Long> itemIds;

  /** 触发人（从安全上下文获取） */
  private String operator;

  private Instant emittedAt;
}
