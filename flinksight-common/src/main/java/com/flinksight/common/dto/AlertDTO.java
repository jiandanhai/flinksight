package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "")
public class AlertDTO implements Serializable {
    private Long id;
    private Long tenantId;
    private Long jobId;
    private Long ruleId;        // 可能为空（未自动匹配到）
    private String level;       // WARN/FATAL...
    private String type;        // CPUHigh/MEMHigh/JobFailed...
    private String message;
    private Integer status;     // 0=未处理 1=处理中 2=关闭
    private Long handlerId;     // 指派/处理人
    private Long clusterId;     // 可能为空
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
