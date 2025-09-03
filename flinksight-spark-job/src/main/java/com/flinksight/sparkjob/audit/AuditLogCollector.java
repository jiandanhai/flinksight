package com.flinksight.sparkjob.audit;

import com.flinksight.common.dto.AuditLogDTO;
import com.flinksight.common.utils.Jsons;
import com.flinksight.sparkjob.sink.DLSink;
import com.flinksight.sparkjob.sink.DynamicSinkFactory;

/**
 * 审计日志采集（推送主平台）（主通道失败自动兜底DLQ）
 * 关键操作审计自动上报，推送主平台
 */
public class AuditLogCollector {
    public static void collect(AuditLogDTO audit) {
        String kafkaBootstrap = System.getProperty("kafka.bootstrap.servers", "localhost:9092");
        String topic = System.getProperty("audit.log.topic", "spark-audit-log");
        String dlqTopic = System.getProperty("audit.log.dlq.topic", "spark-audit-log-dlq");
        String json = Jsons.to(audit);
        try {
            DynamicSinkFactory.applySink(json, "kafka", kafkaBootstrap, topic);
        } catch (Exception ex) {
            DLSink.sendToDLQ(dlqTopic, kafkaBootstrap, json);
        }
    }
}
