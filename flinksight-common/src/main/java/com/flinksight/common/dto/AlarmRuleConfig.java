package com.flinksight.common.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 告警规则配置对象（根据实际配置项增删字段）
 */
@Data
public class AlarmRuleConfig implements Serializable {
    private Long id;
    private String ruleName;
    private String ruleType;
    private String condition;
    private String level;
    private Boolean enabled;
    private String description;
    // ... 其他告警配置字段
}
