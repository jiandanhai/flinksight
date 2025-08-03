package com.flinksight.flinkjob.metrics;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.pulsar.sink.PulsarSink;
import org.apache.flink.connector.pulsar.sink.writer.serializer.PulsarSerializationSchemaWrapper;

import java.util.Properties;

/**
 * 企业级 Pulsar Sink 工厂
 */
public class MetricsPulsarSinkFactory {

    public static PulsarSink<String> build(String serviceUrl, String topic) {
        Properties props = new Properties();
        props.setProperty("pulsar.service.url", serviceUrl);
        return PulsarSink.builder()
                .setServiceUrl(serviceUrl)
                .setTopics(topic)
                .setSerializationSchema(
                        new PulsarSerializationSchemaWrapper<>(new SimpleStringSchema())
                )
                .setProperties(props)
                .build();
    }
}
