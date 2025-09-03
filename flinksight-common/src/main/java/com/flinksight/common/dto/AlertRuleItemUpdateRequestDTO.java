// src/main/java/com/flinksight/backend/dto/AlertRuleItemUpdateRequest.java
package com.flinksight.common.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlertRuleItemUpdateRequestDTO {
  @NotBlank @Size(max = 64)
  private String metricKey;

  @NotBlank
  @Pattern(regexp = "GT|GTE|LT|LTE|EQ|NE")
  private String comparator;

  @NotNull
  private BigDecimal threshold;

  @NotNull @Min(1) @Max(3600)
  private Integer windowSeconds;

  @NotBlank
  @Pattern(regexp = "LAST|AVG|P95|MAX|MIN")
  private String aggregator;

  @NotBlank
  @Pattern(regexp = "INFO|WARN|ERROR|CRITICAL")
  private String severity;

  @NotNull @Min(0) @Max(86_400_000)
  private Integer dedupMs;

  @NotNull
  private Boolean autoRecover;

  /** 可传策略名，服务层会映射为 policyId；允许为空 */
  private String notifyPolicyName;

  /** 1 启用、0 禁用 */
  @NotNull @Min(0) @Max(1)
  private Integer enabled;
}
