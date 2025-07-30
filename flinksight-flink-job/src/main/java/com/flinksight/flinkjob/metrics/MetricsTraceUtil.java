package com.flinksight.flinkjob.metrics;

import com.flinksight.common.dto.JobMetricsEventDTO;
import org.slf4j.MDC;

/**
 * Flink流任务内TraceId埋点与分布式跟踪
 */
public class MetricsTraceUtil {

    /**
     * 获取/生成全局TraceId
     */
    public static String getOrGenTraceId() {
        String tid = MDC.get("traceId");
        if (tid == null) {
            tid = "trace_" + System.currentTimeMillis() + "_" + Thread.currentThread().getId();
            MDC.put("traceId", tid);
        }
        return tid;
    }

    /**
     * 在JobMetricsEvent埋点traceId
     */
    public static void attachTrace(JobMetricsEventDTO event) {
        event.setTraceId(getOrGenTraceId());
    }
}
