package com.flinksight.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertRuleDTO implements Serializable {
    private Long id;
    private String name;        // 规则名
    private Long tenantId;
    private Long clusterId;
    private String metricKey;   // cpu/mem/disk/job...
    private Double threshold;   // 0.85  等
    private String compareOp;   // > >= < <= =
    private String channel;     // email/ding/…
    private Integer enable;     // 1=启用 0=关闭
    private Integer isDeleted;
    private LocalDateTime createdAt;
}
