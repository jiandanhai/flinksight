package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作业告警规则 DTO
 * 用于作业运维、告警策略配置、前后端交互、API等
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobAlertRuleDTO  implements Serializable {

    /** 主键ID */
    private Long id;

    /** 告警规则名称 */
    private String name;

    /** 所属作业ID */
    private Long jobId;

    /** 告警指标类型（如latency、error_rate、cpu、memory等） */
    private String metricType;

    /** 告警条件表达式（如：value > 80） */
    private String condition;

    /** 阈值 */
    private String threshold;

    /** 告警等级（INFO/WARN/ERROR/FATAL） */
    private String level;

    @Schema(description = "报警类型")
    private String alertType;

    /** 告警内容模板（支持变量占位） */
    private String alertTemplate;

    /** 是否启用（true/false） */
    private Boolean enabled;

    /** 多租户隔离字段 */
    private Long tenantId;

    /** 创建人/操作人ID */
    private Long operatorId;

    /** 规则创建时间 */
    private LocalDateTime createdAt;

    /** 规则最后更新时间 */
    private LocalDateTime updatedAt;

    /** 是否删除（软删除标志） */
    private Integer isDeleted;

    /** 审计来源（如：API/页面/定时任务） */
    private String auditSource;

    /** TraceId（全链路追踪ID） */
    private String traceId;
}
