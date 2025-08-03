package com.flinksight.flinkjob.sink;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;

/**
 * 自动化异常捕获和死信队列（DLQ）兜底
 * 死信队列Sink（DLQ），当主Sink异常兜底
 * 支持Kafka DLQ自动写入
 */
public class DLSink {


    /**
     * 构建Kafka DLQ Sink（死信队列）
     * @param dlqTopic         死信队列topic
     * @param kafkaBootstrap   Kafka bootstrap servers
     * @return KafkaSink<String>
     */
    public static KafkaSink<String> build(String dlqTopic, String kafkaBootstrap) {
        return KafkaSink.<String>builder()
                .setBootstrapServers(kafkaBootstrap)
                .setRecordSerializer(
                        KafkaRecordSerializationSchema.builder()
                                .setValueSerializationSchema(new SimpleStringSchema())
                                .setTopic(dlqTopic)
                                .build())
                .build();
    }
}
