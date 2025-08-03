package com.flinksight.common.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobMetricsEventDTO implements Serializable {
    private Long jobId;
    private String jobName;
    private String jobPriority;         // 优先级（HIGH/MEDIUM/LOW 或数字 1-10）
    private Long tenantId;          // 多租户ID（平台隔离必备）
    private String clusterId;       // 所属集群
    private String clusterType;
    private String alertType;       // 报警类型/巡检类型（如 HEALTH_CHECK、DEAD、OOM）
    private String alertLevel;      // 告警级别（INFO/WARN/ERROR/CRITICAL等）
    private String alertMsg;        // 告警内容或描述
    private String metricsJson;     // 其他指标（原始JSON，可选）
    private String traceId;         // 链路追踪ID（可选）
    private String nodeIp;          // 机器IP（可选）
    private String status; // RUNNING/FAILED/SUCCESS
    private Double cpuUsage;
    private Double memoryUsage;
    private Double lag; // 延迟
    private String timestamp;
    private String extraJson;
    private Set<String> notifyChannels; //通知渠道（如：wechat、sms、email等
    // ------- 审计相关补充字段 -------
    @Schema(description = "审计时间（操作产生的UTC时间戳）")
    private LocalDateTime auditAt;

    @Schema(description = "操作人（账号/工号/服务名等）")
    private String operator;

    @Schema(description = "审计来源（如API、WEB、自动化脚本、定时任务等）")
    private String auditSource;
    /**
     * 是否触发自动恢复(自愈)，1为是，0为否
     */
    private boolean autoRecover;
    // 还可以加 jobStatus、userId、env 等等

    public static JobMetricsEventDTO fromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, JobMetricsEventDTO.class);
        } catch (Exception e) {
            return null;
        }
    }
}
