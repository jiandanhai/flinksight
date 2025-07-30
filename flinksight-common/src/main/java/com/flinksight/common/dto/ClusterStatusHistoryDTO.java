    package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 集群状态历史 DTO（ClusterStatusHistoryDTO）
 * 用于作业平台/监控平台/任务诊断等数据传递
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusterStatusHistoryDTO {

    /** 主键ID */
    private Long id;

    /** 集群ID（对应 cluster 表主键） */
    private Long clusterId;

    /** 当前状态（如：ACTIVE/INACTIVE/FAILED/RECOVERING/UPGRADING） */
    private String status;

    /** 状态变更描述（如异常原因、升级内容等） */
    private String description;

    /** 发生时间 */
    private LocalDateTime occurredAt;

    /** 租户ID */
    private Long tenantId;

    /** 记录创建时间 */
    private LocalDateTime createdAt;

    /** 软删除标志 */
    private Integer isDeleted;

    /** 审计/操作来源（如API/自动巡检/后台运维） */
    private String auditSource;

    /** TraceID（链路追踪） */
    private String traceId;

    @Schema(description = "采集时间")
    private LocalDateTime collectTime;

    @Schema(description = "当前活跃节点数")
    private Integer activeNodeCount;

    @Schema(description = "CPU使用率")
    private Double cpuUsage;

    @Schema(description = "内存使用率")
    private Double memoryUsage;

    @Schema(description = "队列/命名空间/队列负载")
    private String queueLoadJson;

    @Schema(description = "资源池扩展（可JSON存）")
    private String extendJson;
}
