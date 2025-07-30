package com.flinksight.flinkjob.audit;

import com.flinksight.common.dto.JobMetricsEventDTO;
import org.apache.flink.api.common.functions.RichMapFunction;
import java.time.LocalDateTime;

/**
 * Flink流内审计与安全埋点
 * Flink流内审计日志埋点，包括租户/操作人/来源页面/操作类型等
 * 注：event对象请在common中补全operator、auditAt、auditSource等审计字段。
 */
public class AuditLogMapFunction extends RichMapFunction<JobMetricsEventDTO, JobMetricsEventDTO> {
    @Override
    public JobMetricsEventDTO map(JobMetricsEventDTO event) throws Exception {
        event.setAuditAt(LocalDateTime.now());
        event.setOperator("flink-job"); // 可从上下文注入
        event.setAuditSource("ops-monitor"); // 可扩展实际页面/来源
        return event;
    }
}
