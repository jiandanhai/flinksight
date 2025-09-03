package com.flinksight.flinkjob.collector;

import com.flinksight.common.dto.JobMetricsEventDTO; // 你的 DTO
import com.flinksight.common.utils.Jsons;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Flink REST 拉取 Job/Vertex 指标，生成你的 JobMetricsEventDTO，写入 Kafka。
 * Runner：Flink REST → JobMetricsEventDTO(JSON) → Kafka
 */
public class FlinkMetricsCollectorJob {

  public static void main(String[] args) throws Exception {
    final ParameterTool p = ParameterTool.fromArgs(args);

    // ===== 平台/多租户参数 =====
    final String restUrl    = p.get("rest.url",      "http://127.0.0.1:8081");
    final long   tenantId   = Long.parseLong(p.get("tenant.id", "1"));
    final String envLabel   = p.get("env",           "dev");
    final String cluster    = p.get("cluster",       "flink-demo");
    final String clusterId  = p.get("cluster.id",    "flink-1");
    final String clusterTyp = p.get("cluster.type",  "standalone");
    final long   intervalMs = Long.parseLong(p.get("interval.ms", "15000"));

    // ===== Kafka 出口 =====
    final String brokers = p.get("kafka.brokers", "localhost:9092");
    final String topic   = p.get("kafka.topic",   "flinksight.flink.metrics");

    // ===== Flink 环境 =====
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.enableCheckpointing(30_000);

    // ===== 新 Source API：fromSource =====
    final DataStream<JobMetricsEventDTO> ds = env.fromSource(
            new FlinkRestSource(restUrl, tenantId, envLabel, cluster, clusterId, clusterTyp, intervalMs),
            WatermarkStrategy.<JobMetricsEventDTO>noWatermarks(),
            "flink-rest-source"
    );

    // DTO → JSON 串
    final DataStream<String> json = ds.map(Jsons::to).name("map:dto->json");

    // ===== KafkaSink（3.x for Flink 1.18）=====
    final KafkaSink<String> sink = KafkaSink.<String>builder()
            .setBootstrapServers(brokers)
            .setRecordSerializer(
                    KafkaRecordSerializationSchema.builder()
                            .setTopic(topic)
                            .setValueSerializationSchema(new SimpleStringSchema())
                            .build()
            )
            .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
            .build();

    json.sinkTo(sink).name("kafka:" + topic);

    env.execute("Flinksight - Flink Metrics Collector (SourceAPI)");
  }
}