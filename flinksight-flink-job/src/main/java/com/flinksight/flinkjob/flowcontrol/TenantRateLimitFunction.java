package com.flinksight.flinkjob.flowcontrol;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * 流控/限流（流量峰值保护、租户隔离） Flink内置 RateLimiter 函数（可自定义全局/分租户限流）
 * 分租户限流（滑动窗口计数）
 * 使用方式（在作业主流加 keyBy(tenantId).process(new TenantRateLimitFunction(100))）
 */
public class TenantRateLimitFunction extends KeyedProcessFunction<Long, String, String> {
    private final int maxPerSecond;

    public TenantRateLimitFunction(int maxPerSecond) {
        this.maxPerSecond = maxPerSecond;
    }

    @Override
    public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
        ValueState<Long> lastTs = getRuntimeContext().getState(new ValueStateDescriptor<>("lastTs", Long.class));
        ValueState<Integer> count = getRuntimeContext().getState(new ValueStateDescriptor<>("count", Integer.class));
        long now = System.currentTimeMillis() / 1000;
        if (lastTs.value() == null || lastTs.value() != now) {
            lastTs.update(now);
            count.update(1);
            out.collect(value);
        } else if (count.value() < maxPerSecond) {
            count.update(count.value() + 1);
            out.collect(value);
        }
        // else 丢弃或缓冲，可自定义
    }
}
