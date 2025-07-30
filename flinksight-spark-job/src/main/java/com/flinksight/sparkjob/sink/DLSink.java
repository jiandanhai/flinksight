package com.flinksight.sparkjob.sink;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

/**
 * 功能说明：主业务推送Kafka等如遇异常，将消息兜底投递DLQ，避免数据丢失。平台侧可定时扫描DLQ进行告警和补偿。
 * 死信队列（DLQ）Sink，所有主通道异常兜底。
 * 支持自动将告警、指标、操作审计等失败消息写入DLQ。
 * 使用方式：在主Sink捕获到异常时调用DLSink.sendToDLQ(...)，topic如spark-job-dlq。
 */
public class DLSink {

    /**
     * 发送消息到Kafka死信队列DLQ
     * @param dlqTopic 死信队列topic
     * @param kafkaBootstrap kafka地址
     * @param message 原始消息体（JSON）
     */
    public static void sendToDLQ(String dlqTopic, String kafkaBootstrap, String message) {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaBootstrap);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            producer.send(new ProducerRecord<>(dlqTopic, message));
        } catch (Exception ex) {
            // 兜底异常处理，平台监控可告警
            System.err.println("DLQ推送失败: " + ex.getMessage());
        }
    }
}
