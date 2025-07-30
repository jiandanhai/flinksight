package com.flinksight.flinkjob.idempotent;

import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

/**
 * 分布式幂等事件处理
 * 常见场景：同一告警/恢复事件只推送一次，支持批处理/流式
 *
 */
public class EventIdempotentFunction extends KeyedProcessFunction<String, String, String> {
    @Override
    public void processElement(String value, Context ctx, Collector<String> out) throws Exception {
        // 以事件ID为key（如traceId+alertType）
        ValueStateDescriptor<Boolean> seenDesc = new ValueStateDescriptor<>("seen", Boolean.class);
        var seen = getRuntimeContext().getState(seenDesc);
        if (seen.value() == null || !seen.value()) {
            out.collect(value);
            seen.update(true);
        }
        // 可设置TTL，清理旧幂等状态
    }
}
