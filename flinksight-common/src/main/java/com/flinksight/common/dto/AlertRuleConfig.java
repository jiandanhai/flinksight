package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 动态报警规则配置实体
 * 支持每个作业/租户单独规则、可动态热更新
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "动态报警规则DTO")
public class AlertRuleConfig implements Serializable {
    private Map<Long, JobAlertRule> jobRules; // key: jobId, value: 规则
    private long version; // 配置版本号

    @Data
    public static class JobAlertRule implements Serializable {
        private Long jobId;
        private Double lagThreshold;    // 允许最大延迟
        private String alertLevel;      // 报警等级
        private Set<String> notifyChannels;// 报警通道
        private boolean enableAutoRecover; // 自动自愈
    }
}
