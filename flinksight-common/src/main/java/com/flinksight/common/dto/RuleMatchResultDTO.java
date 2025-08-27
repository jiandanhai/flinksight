package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "规则测试匹配结果（含评分与解释）")
public class RuleMatchResultDTO {
    private AlertRuleDTO rule;
    private int score;
    private boolean matchedCluster;
    private boolean matchedMetric;
    private boolean matchedOp;
    private Double thresholdDiff; // 绝对误差；null 表示无法计算
}