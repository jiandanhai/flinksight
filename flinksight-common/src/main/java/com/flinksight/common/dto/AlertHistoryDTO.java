package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报警历史DTO
 */
@Data
@Schema(description = "报警历史DTO")
public class AlertHistoryDTO implements Serializable {
    private Long id;
    private Long alertId;
    private Long ruleId;
    private String content;
    private Integer level;
    private Integer status;
    private Long operatorId;
    private Long tenantId;
    private LocalDateTime operateTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
}
