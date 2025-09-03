// package com.flinksight.backend.dto
package com.flinksight.common.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class RuleSnapshotDTO {
  private Long tenantId;
  private Scope scope;
  private Long version;    // 当前激活版本号
  private String etag;     // W/"sha256:..."
  private List<Item> rules;

  @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
  public static class Scope {
    private String type;   // TENANT/CLUSTER/JOB
    private Long id;       // TENANT 级为 null
  }

  @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
  public static class Item {
    private String metric;
    private String comparator;
    private BigDecimal threshold;
    private Integer windowSeconds;
    private String aggregator;
    private String severity;
    private Integer dedupMs;
    private Boolean autoRecover;
    private String notifyPolicy; // 名称（作业端再映射具体 webhook）
  }
}
