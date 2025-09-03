package com.flinksight.sparkjob.metrics;

import com.flinksight.common.dto.JobMetricsEventDTO;
import com.flinksight.common.utils.Jsons;
import com.flinksight.common.utils.TraceUtil;
import com.flinksight.sparkjob.sink.DLSink;
import com.flinksight.sparkjob.sink.DynamicSinkFactory;

/**
 * 指标推送（与flinksight-common DTO配合）
 * 指标推送服务：把采集到的JobMetricsEvent推送到主平台Kafka/HTTP等
 * 指标推送服务，主通道异常自动兜底写DLQ
 * 并为每个事件分配唯一traceId保障幂等
 * 主通道指标推送 + 幂等 + DLQ兜底
 * **说明：**平台Kafka消费方需以traceId做唯一索引去重（如唯一键、Redis/DB判重），保障全局幂等。
 */
public class MetricsReporter {
    public static void report(JobMetricsEventDTO event) {
        // 分配全局唯一traceId，用于平台幂等处理（如UUID/雪花ID）
        if (event.getTraceId() == null || event.getTraceId().isEmpty()) {
            event.setTraceId(TraceUtil.generateTraceId());
        }

        String sinkType = System.getProperty("sink.type", "kafka");
        String bootstrap = System.getProperty("kafka.bootstrap.servers", "localhost:9092");
        String topic = System.getProperty("job.metrics.topic", "spark-job-metrics");
        String dlqTopic = System.getProperty("job.metrics.dlq.topic", "spark-job-metrics-dlq");

        String json = Jsons.to(event);
        try {
            // 主通道推送
            DynamicSinkFactory.applySink(json, sinkType, bootstrap, topic);
        } catch (Exception ex) {
            // 主通道异常时自动写入DLQ
            DLSink.sendToDLQ(dlqTopic, bootstrap, json);
        }
    }
}
