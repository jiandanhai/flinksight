package com.flinksight.sparkjob.collector;

import com.flinksight.common.dto.JobMetricsEventDTO;
import com.flinksight.common.tenant.TenantContext;
import com.flinksight.common.utils.TraceUtil;
import com.flinksight.sparkjob.metrics.MetricsReporter;
import org.apache.spark.scheduler.*;

import java.time.Instant;

/**
 * Spark作业多租户指标采集Listener
 */
public class SparkMetricsCollector extends SparkListener {
    private final String jobName;
    private final Long tenantId;
    private final String operator;

    public SparkMetricsCollector(String jobName, Long tenantId, String operator) {
        this.jobName = jobName;
        this.tenantId = tenantId;
        this.operator = operator;
    }

    @Override
    public void onJobStart(SparkListenerJobStart jobStart) {
        TenantContext.setTenantId(tenantId);
        JobMetricsEventDTO event = JobMetricsEventDTO.builder()
                .jobName(jobName)
                .tenantId(tenantId)
                .status("RUNNING")
                .timestamp(String.valueOf(Instant.now().toEpochMilli()))
                .operator(operator)
                .traceId(TraceUtil.generateTraceId())
                .build();
        MetricsReporter.report(event);
    }

    @Override
    public void onJobEnd(SparkListenerJobEnd jobEnd) {
        TenantContext.setTenantId(tenantId);
        JobMetricsEventDTO event = JobMetricsEventDTO.builder()
                .jobName(jobName)
                .tenantId(tenantId)
                .status(jobEnd.jobResult().toString())
                .timestamp(String.valueOf(Instant.now().toEpochMilli()))
                .operator(operator)
                .traceId(TraceUtil.generateTraceId())
                .build();
        MetricsReporter.report(event);
    }
}
