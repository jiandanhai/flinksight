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
public class JobMetricDTO implements Serializable {
    private Long id;
    private Long tenantId;
    private Long jobId;
    private String metricKey;
    private Double value;
    private LocalDateTime metricTime;
    private Integer isDeleted;
}
