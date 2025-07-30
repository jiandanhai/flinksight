package com.flinksight.sparkjob.sink;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

/**
 * 动态Sink工厂（Kafka/Pulsar/HTTP）
 * 动态Sink工厂，按需推Kafka/Pulsar/HTTP等
 */
public class DynamicSinkFactory {

    public static void applySink(String eventJson, String sinkType, String bootstrap, String topic) {
        if ("kafka".equalsIgnoreCase(sinkType)) {
            Properties props = new Properties();
            props.put("bootstrap.servers", bootstrap);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
                producer.send(new ProducerRecord<>(topic, eventJson));
            }
        }
        // ...Pulsar、HTTP等自定义sink
    }
}
