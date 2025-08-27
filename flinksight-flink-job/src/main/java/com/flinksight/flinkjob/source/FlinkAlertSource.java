package com.flinksight.flinkjob.source;

import com.flinksight.common.dto.AlertDTO;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.time.LocalDateTime;
import java.util.Random;

public class FlinkAlertSource extends RichSourceFunction<AlertDTO> {
    private volatile boolean running = true;
    private final Long tenantId;

    public FlinkAlertSource(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public void run(SourceContext<AlertDTO> ctx) throws Exception {
        Random random = new Random();
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("alert_topic")
                .setGroupId("alert-group")
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        String[] messages = {"CPU overload", "Memory leak", "Disk failure", "Network timeout"};
        while (running) {
            AlertDTO alert = new AlertDTO();
            alert.setId((long) random.nextInt(10000));
            alert.setTenantId(tenantId);
            alert.setMessage(messages[random.nextInt(messages.length)]);
            alert.setCreatedAt(LocalDateTime.now());
            alert.setUpdatedAt(LocalDateTime.now());
            ctx.collect(alert);
            Thread.sleep(1000); // 每秒一条
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}