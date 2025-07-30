package com.flinksight.flinkjob.metrics;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.datastream.DataStream;
import java.time.LocalDateTime;

/**
 * 作业流健康探针与心跳上报
 * 健康探针上报，周期性生成心跳/健康流，便于前后端健康探测与断线重连
 */
public class JobHealthHeartbeat {

    public static DataStream<String> createHeartbeat(StreamExecutionEnvironment env, String jobName, long intervalMs) {
        return env.fromSequence(1, Long.MAX_VALUE)
                .map(seq -> String.format("{\"jobName\":\"%s\",\"heartbeatAt\":\"%s\",\"seq\":%d}",
                        jobName, LocalDateTime.now(), seq))
                .setParallelism(1)
                .assignTimestampsAndWatermarks(WatermarkStrategy.noWatermarks())
                .filter(s -> System.currentTimeMillis() % intervalMs < 100); // 近似周期心跳
    }
}
