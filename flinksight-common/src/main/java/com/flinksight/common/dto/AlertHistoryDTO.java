package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报警历史DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "报警历史DTO")
public class AlertHistoryDTO implements Serializable {
    private Long id;
    private Long alertId;
    private Long ruleId;
    private String content;        // 处理内容/备注
    private Integer level;         // 快照级别
    private Integer status;        // 快照状态（0/1/2）
    private Long operatorId;       // 操作人
    private Long tenantId;
    private LocalDateTime operateTime;
    private Integer isDeleted;
    private LocalDateTime createdAt;
}
