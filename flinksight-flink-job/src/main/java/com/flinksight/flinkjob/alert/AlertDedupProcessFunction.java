package com.flinksight.flinkjob.alert;

import com.flinksight.common.dto.JobMetricsEventDTO;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * Flink流内异常事件聚合去重（防抖）
 * 异常报警流防抖处理，防止短时大量重复告警（比如网络抖动、集群波动）
 */
public class AlertDedupProcessFunction extends KeyedProcessFunction<String, JobMetricsEventDTO, JobMetricsEventDTO> {
    private final long dedupIntervalMs;

    public AlertDedupProcessFunction(long dedupIntervalMs) {
        this.dedupIntervalMs = dedupIntervalMs;
    }

    @Override
    public void processElement(JobMetricsEventDTO event, Context ctx, Collector<JobMetricsEventDTO> out) throws Exception {
        String key = event.getJobId() + "_" + event.getAlertType();
        ValueStateDescriptor<Long> lastFireDesc = new ValueStateDescriptor<>("lastFire", Long.class);
        var lastFire = getRuntimeContext().getState(lastFireDesc);
        Long lastTs = lastFire.value();
        long now = System.currentTimeMillis();
        if (lastTs == null || now - lastTs > dedupIntervalMs) {
            out.collect(event);
            lastFire.update(now);
        }
    }
}
