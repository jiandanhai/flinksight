// src/main/java/com/flinksight/backend/dto/RuleSetDetailDTO.java
package com.flinksight.common.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RuleSetDetailDTO {
  private Long id;
  private Long tenantId;
  private String scopeType;
  private Long scopeId;
  private Long version;
  private String status;
  private Integer activeFlag;
  private String checksum;
  private String createdBy;
  private LocalDateTime createdAt;
  private List<AlertRuleItemDTO> items; // 该版本规则项的完整列表
}
