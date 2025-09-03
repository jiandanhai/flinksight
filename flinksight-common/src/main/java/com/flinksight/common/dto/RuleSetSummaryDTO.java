// src/main/java/com/flinksight/backend/dto/RuleSetSummaryDTO.java
package com.flinksight.common.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RuleSetSummaryDTO {
  private Long id;
  private Long tenantId;
  private String scopeType;   // TENANT/CLUSTER/JOB
  private Long scopeId;       // TENANT 级为 null
  private Long version;       // 业务版本号
  private String status;      // DRAFT/ACTIVE/ARCHIVED
  private Integer activeFlag; // 0/1
  private String checksum;    // sha256 指纹
  private String createdBy;
  private LocalDateTime createdAt;
  private Long itemCount;     // 该版本下规则数
}
