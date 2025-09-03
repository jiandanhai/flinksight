package com.flinksight.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 规则项：表达指标/比较/阈值/窗口/聚合/等级/通知/自愈等
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertRuleItemDTO implements Serializable {
    private Long id;
    private Long ruleSetId;
    private String metricKey;
    private String comparator;
    private BigDecimal threshold;
    private Integer windowSeconds;
    private String aggregator;
    private String severity;
    private Integer dedupMs;
    private Integer autoRecover;
    private String notifyPolicyName; // 便于页面显示
    private Integer enabled;
    private Integer isDeleted;
    private LocalDateTime createdAt;
}
