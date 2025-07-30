package com.flinksight.flinkjob.runner;

import com.flinksight.common.dto.JobMetricsEventDTO;
import com.flinksight.common.dto.AlertRuleConfig;
import com.flinksight.common.utils.JsonUtil;
import com.flinksight.flinkjob.alert.*;
import com.flinksight.flinkjob.audit.AuditLogMapFunction;
import com.flinksight.flinkjob.config.DynamicContextHelper;
import com.flinksight.flinkjob.metrics.*;
import com.flinksight.flinkjob.recovery.RecoveryManager;
import com.flinksight.flinkjob.sink.DynamicSinkFactory;
import com.flinksight.flinkjob.sink.FlinkAlertSink;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * flinksight 生产级运维监控Flink作业（商业SaaS级全链路实现）
 * - 多环境多租户动态参数
 * - 企业级报警规则API联动
 * - 审计与Trace全链路
 * - 动态报警Sink支持Kafka/ES/Pulsar/Hudi/HDFS
 * - 生产可观测性/自动自愈/流控等
 */
public class FlinkOpsJob {

    public static void main(String[] args) throws Exception {
        // 1. 动态参数和环境适配
        ParameterTool params = ParameterTool.fromArgs(args);

        String kafkaBootstrap = params.get("kafka.bootstrap.servers", "localhost:9092");
        String jobMetricsTopic = params.get("job.metrics.topic", "job-metrics");
        String jobAlertTopic = params.get("job.alert.topic", "job-alert");
        String heartbeatTopic = params.get("job.heartbeat.topic", "job-heartbeat");

        // Sink参数支持独立配置，支持多种后端热切换
        String alertSinkType = params.get("alert.sink.type", "kafka");
        String alertSinkParam = params.get("alert.sink.param", kafkaBootstrap);
        String metricsSinkType = params.get("metrics.sink.type", "kafka");
        String metricsSinkParam = params.get("metrics.sink.param", kafkaBootstrap);
        String heartbeatSinkType = params.get("heartbeat.sink.type", "kafka");
        String heartbeatSinkParam = params.get("heartbeat.sink.param", kafkaBootstrap);
        long dedupIntervalMs = params.getLong("alert.dedup.ms", 30000);

        String jobName = params.get("job.name", "flinksight-ops-job");
        String envLabel = DynamicContextHelper.getEnvLabel(params);

        // 2. 环境和容错参数配置
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(60000, CheckpointingMode.EXACTLY_ONCE);

        // 3. 健康心跳流，实时可观测与自动平台探针
        DataStream<String> heartbeat = JobHealthHeartbeat.createHeartbeat(env, jobName, 60000);
        DynamicSinkFactory.applySink(heartbeat, heartbeatSinkType, heartbeatSinkParam, env);

        // 4. 消费Kafka Job Metrics指标流
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(kafkaBootstrap)
                .setTopics(jobMetricsTopic)
                .setGroupId(params.get("kafka.group.id", "flinksight-ops-group"))
                .setValueOnlyDeserializer(new org.apache.flink.api.common.serialization.SimpleStringSchema())
                .build();

        DataStream<String> raw = env.fromSource(source, WatermarkStrategy.noWatermarks(), "JobMetricsKafkaSource");

        // 5. DTO映射，Trace埋点（每条链路全追踪）
        DataStream<JobMetricsEventDTO> events = raw.map(new RichMapFunction<String, JobMetricsEventDTO>() {
            @Override
            public JobMetricsEventDTO map(String value) {
                JobMetricsEventDTO event = JsonUtil.fromJson(value, JobMetricsEventDTO.class);
                TraceIdPropagator.propagate(event);
                return event;
            }
        });

        // 6. 多租户分流、全链路审计埋点
        DataStream<JobMetricsEventDTO> audited = events
                .keyBy(JobMetricsEventDTO::getTenantId)
                .map(new AuditLogMapFunction());

        // 7. 企业平台API动态报警规则广播（支持热更新与多租户策略）
        String ruleApi = params.get("alert.rule.api", "http://alert-api/rules/latest");
        BroadcastStream<AlertRuleConfig> rulesBroadcast = env
                .addSource(new AlertRuleApiSource(ruleApi))
                .broadcast(DynamicAlertRuleBroadcastProcessFunction.RULES_DESC);

        // 8. 指标流下沉（大屏/Prometheus/ES等，可独立配置）
        DataStream<String> metricsJson = audited.map(JsonUtil::toJson);
        DynamicSinkFactory.applySink(metricsJson, metricsSinkType, metricsSinkParam, env);

        // 9. 动态报警规则联动、报警流防抖去重（全业务逻辑流）
        SingleOutputStreamOperator<JobMetricsEventDTO> alertEvents = audited
                .connect(rulesBroadcast)
                .process(new DynamicAlertRuleBroadcastProcessFunction())
                .keyBy(e -> e.getJobId() + "_" + e.getAlertType())
                .process(new AlertDedupProcessFunction(dedupIntervalMs));

        // 10. 报警流下沉（Kafka/ES/Pulsar/Hudi/HDFS等，支持平台配置化切换）
        FlinkAlertSink.sink(alertEvents, alertSinkType, alertSinkParam, env);

        // 11. 自动恢复自愈（生产级自动化闭环）
        raw.map(RecoveryManager::processIfFailed);

        env.execute("flinksight-ops 生产级全功能流作业");
    }
}
