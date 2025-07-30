package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作业报警日志 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "作业报警日志DTO")
public class JobAlertLogDTO implements Serializable {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "作业ID")
    private Long jobId;

    @Schema(description = "报警级别（INFO/WARN/ERROR/CRITICAL）")
    private String alertLevel;

    @Schema(description = "报警类型（如异常类型、指标告警等）")
    private String alertType;

    @Schema(description = "报警内容（消息文本/markdown）")
    private String alertMsg;

    @Schema(description = "报警时间")
    private LocalDateTime alertAt;

    @Schema(description = "告警推送渠道（如DingDing/WeChat/SMS/Email）")
    private String notifyChannels;

    @Schema(description = "报警目标用户/组（userId或groupId）")
    private String target;

    @Schema(description = "报警来源（如监控、手动、系统）")
    private String alertSource;

    @Schema(description = "报警处理状态（PENDING/ACK/RESOLVED/IGNORED）")
    private String alertStatus;

    @Schema(description = "关联租户ID")
    private Long tenantId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "最后更新时间")
    private LocalDateTime updateTime;
    @Schema(description = "软删除标志")
    private Integer isDeleted;
}
