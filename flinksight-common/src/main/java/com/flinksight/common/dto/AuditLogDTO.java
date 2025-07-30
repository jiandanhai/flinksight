package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 审计日志DTO
 * 适用于操作审计、平台安全追溯、异常溯源等场景
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "操作审计日志DTO")
public class AuditLogDTO {

    @Schema(description = "日志主键ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "操作时间")
    private LocalDateTime operateTime;

    @Schema(description = "操作人ID")
    private String operatorId;

    @Schema(description = "操作人名称")
    private String operatorName;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "操作类型（如CREATE/DELETE/EXPORT/LOGIN等）")
    private String action;

    @Schema(description = "目标类型（如User、Job、Cluster等）")
    private String targetType;

    @Schema(description = "目标对象ID")
    private String targetId;

    @Schema(description = "操作内容/详情（如变更前后、请求参数等）")
    private String content;

    @Schema(description = "前端来源页面、接口")
    private String source;

    @Schema(description = "traceId，全链路追踪标识")
    private String traceId;

    @Schema(description = "操作结果（SUCCESS/FAIL等）")
    private String result;

    @Schema(description = "失败原因（如有）")
    private String failReason;

    @Schema(description = "ip地址")
    private String ip;
    @Schema(description = "注册时间")
    private LocalDateTime createTime;

    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted;
}
