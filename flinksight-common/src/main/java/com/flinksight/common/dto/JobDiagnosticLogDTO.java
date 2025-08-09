package com.flinksight.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作业诊断日志 DTO
 * 用于运维日志、异常分析、自动诊断、全链路追踪等
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDiagnosticLogDTO  implements Serializable {

    /** 日志ID */
    private Long id;

    /** 所属作业ID */
    private Long jobId;

    /** 所属作业名称 */
    private String jobName;

    /** 日志记录类型（INFO/WARN/ERROR/DIAGNOSIS） */
    private String logLevel;

    /** 日志内容/诊断详情 */
    private String logContent;

    /** 诊断结论/建议（如：异常原因、优化建议等） */
    private String suggestion;

    /** 记录采集时间 */
    private LocalDateTime logTime;

    /** 日志来源（如：Flink、Spark、手动、定时巡检等） */
    private String source;

    /** 相关节点/实例ID（如 taskmanager、executor等） */
    private String nodeId;

    /** 多租户隔离字段 */
    private Long tenantId;

    /** 采集/创建人 */
    private Long operatorId;

    /** 审计来源（页面、API、自动化脚本等） */
    private String auditSource;

    /** TraceId（链路追踪ID） */
    private String traceId;

    /** 是否删除（软删除） */
    private Integer isDeleted;

    /** 记录创建时间 */
    private LocalDateTime createdAt;

    /** 最后更新时间 */
    private LocalDateTime updatedAt;
}
