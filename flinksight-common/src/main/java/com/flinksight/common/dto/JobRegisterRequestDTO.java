package com.flinksight.common.dto;

import lombok.*;
import java.io.Serializable;

/**
 * Spark/Flink作业自动注册请求DTO
 * 支持多租户、权限、链路追踪、备注、自动注册幂等
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRegisterRequestDTO implements Serializable {

    /**
     * 平台侧全局唯一作业名（英文、平台唯一）
     */
    private String jobName;

    /**
     * 作业ID（选填，平台分配/外部传入均可）
     */
    private Long jobId;

    /**
     * 租户ID，支持多租户隔离
     */
    private Long tenantId;

    /**
     * 作业类型（如 SPARK、FLINK、BATCH、STREAMING）
     */
    private String jobType;

    /**
     * 作业所属项目或空间标识（如projectId、namespace等）
     */
    private String projectCode;

    /**
     * 注册操作人（可追溯）
     */
    private String operator;

    /**
     * 注册来源（如 auto-register/sync/manual）
     */
    private String source;

    /**
     * 作业注册时的链路追踪ID（平台全局唯一）
     */
    private String traceId;

    /**
     * 额外备注（如作业描述、标签等）
     */
    private String remark;

    /**
     * 注册时间（毫秒时间戳）
     */
    private Long registerAt;
}
