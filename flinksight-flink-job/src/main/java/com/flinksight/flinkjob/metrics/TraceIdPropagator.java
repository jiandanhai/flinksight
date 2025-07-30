package com.flinksight.flinkjob.metrics;

import com.flinksight.common.dto.JobMetricsEventDTO;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * 报警/恢复/指标流的Trace链路全生命周期标识
 * TraceId全流程分发与链路追踪
 */
public class TraceIdPropagator {

    public static String getOrCreateTraceId() {
        String traceId = MDC.get("traceId");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
            MDC.put("traceId", traceId);
        }
        return traceId;
    }

    public static void propagate(JobMetricsEventDTO event) {
        event.setTraceId(getOrCreateTraceId());
    }
}
