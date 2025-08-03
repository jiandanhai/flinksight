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
    private Long tenantId;
    private Long clusterId;
    private String metricKey;
    private Double threshold;
    private String compareOp;
    private String channel;
    private Integer enable;
    private Integer isDeleted;
    private LocalDateTime createTime;
}
