package com.flinksight.sparkjob.runner;

import com.flinksight.common.dto.JobMetricsEventDTO;
import com.flinksight.common.utils.JobIdCodec;
import com.flinksight.common.utils.Jsons;
import com.flinksight.common.utils.TimeUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.spark.executor.TaskMetrics;
import org.apache.spark.scheduler.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 在 Driver 侧监听 Spark 事件，组装 JobMetricsEventDTO 并写入 Kafka。
 * - jobId 使用 JobIdCodec 将 appId 稳定映射为 Long；
 * - lag 暂无直接来源，留空或由后端计算；如你有定义可在 metricsJson 中带入。
 *  Stage 指标通过 onTaskEnd 聚合 TaskMetrics（input/shuffle 读写字节），在 onStageCompleted 输出汇总。
 *  通过 Kafka 输出 JSON（与 FlinkOpsJob 对接）。
 *
 */
public class FlinksightSparkListener extends SparkListener {
    private final KafkaProducer<String,String> producer; private final String topic;
    private final long tenantId; private final String env; private final String cluster; private final String clusterId; private final String clusterType;
    private final AtomicReference<Long> jobIdRef = new AtomicReference<>();
    private final String appName; private final String appIdRaw;

    // 存储阶段提交时间
    private final Map<Integer, Long> stageSubmitTimes = new HashMap<>();
    // 存储阶段任务的指标
    private final Map<Long, Long> stageInputBytes = new HashMap<>();
    private final Map<Long, Long> stageShuffleReadBytes = new HashMap<>();
    private final Map<Long, Long> stageShuffleWriteBytes = new HashMap<>();

    public FlinksightSparkListener(String brokers, String topic, long tenantId, String env, String cluster, String clusterId, String clusterType, String appName, String appId){
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("enable.idempotence", "true"); props.put("acks", "all");
        this.producer = new KafkaProducer<>(props);
        this.topic = topic; this.tenantId=tenantId; this.env=env; this.cluster=cluster; this.clusterId=clusterId; this.clusterType=clusterType;
        this.appName = appName; this.appIdRaw = appId; this.jobIdRef.set(JobIdCodec.toLongOrNull(appId));
    }

    private void emit(String status, Map<String,Object> extra){
        Map<String,Object> metrics = new LinkedHashMap<>(extra==null? Collections.emptyMap():extra);
        try {
            JobMetricsEventDTO dto = JobMetricsEventDTO.builder()
                    .jobId(jobIdRef.get())
                    .jobName(appName)
                    .tenantId(tenantId)
                    .env(env)
                    .engine("SPARK")
                    .cluster(cluster).clusterId(clusterId).clusterType(clusterType)
                    .status(status)
                    .timestamp(TimeUtil.nowIso())
                    .metricsJson(Jsons.to(metrics))
                    .build();
            producer.send(new ProducerRecord<>(topic, String.valueOf(dto.getJobId()), Jsons.to(dto)));
        } catch (Exception ignore) {}
    }

    @Override public void onApplicationStart(SparkListenerApplicationStart appStart) {
        emit("RUNNING", Map.of("event","app_start","appId", appIdRaw));
    }
    @Override public void onJobStart(SparkListenerJobStart jobStart) {
        emit("RUNNING", Map.of("event","job_start","jobId", jobStart.jobId()));
    }


    @Override public void onApplicationEnd(SparkListenerApplicationEnd appEnd) {
        emit("SUCCESS", Map.of("event","app_end","time", appEnd.time()));
    }


    @Override
    public void onStageSubmitted(SparkListenerStageSubmitted stageSubmitted) {
        // 记录阶段提交时间
        stageSubmitTimes.put(stageSubmitted.stageInfo().stageId(), System.nanoTime());
    }

    @Override
    public void onStageCompleted(SparkListenerStageCompleted stageCompleted) {
        var s = stageCompleted.stageInfo();
        // 获取提交时间和完成时间，并进行类型转换
        Object submitTimeObj = stageCompleted.stageInfo().submissionTime().get();
        Object completionTimeObj = stageCompleted.stageInfo().completionTime().get();

        // 转换为 long 类型
        long submitTime = submitTimeObj instanceof Long ? submitTime = (Long) submitTimeObj : 0L;
        long completionTime = submitTimeObj instanceof Long ? completionTime = (Long) completionTimeObj : 0L;

        // 计算阶段持续时间
        long duration = completionTime - submitTime;
        System.out.println("Stage duration: " + duration + " milliseconds");
        // 获取 taskMetrics 数据（在 Spark 3.x 中，taskMetrics 是通过 task 事件获得的）
        long inputBytes = 0L;
        long shuffleReadBytes = 0L;
        long shuffleWriteBytes = 0L;

        // 获取任务的相关指标（这些需要通过 Task 监听器来处理）
        // 这里只是演示，如果需要获取真实的 taskMetrics 数据，你需要从任务事件中获取
        TaskMetrics taskMetrics = stageCompleted.stageInfo().taskMetrics();
        if (taskMetrics!= null) {
            // 从 inputMetrics 获取 bytesRead，表示输入字节数
            inputBytes = taskMetrics.inputMetrics() != null ? taskMetrics.inputMetrics().bytesRead() : 0L;
            // 从 shuffleReadMetrics 获取 totalBytesRead，表示 Shuffle 读取字节数
            shuffleReadBytes = taskMetrics.shuffleReadMetrics() != null ? taskMetrics.shuffleReadMetrics().totalBytesRead() : 0L;
            // 从 shuffleWriteMetrics 获取 totalBytesWritten，表示 Shuffle 写入字节数
            shuffleWriteBytes = taskMetrics.shuffleWriteMetrics() != null ? taskMetrics.shuffleWriteMetrics().bytesWritten(): 0L;
        }

        // 发送数据到目标
        emit("RUNNING", Map.of(
                "event", "stage_completed",
                "stageId", s.stageId(),
                "name", s.name(),
                "tasks", s.numTasks(),
                "duration", duration,
                "inputBytes", inputBytes,
                "shuffleRead", shuffleReadBytes,
                "shuffleWrite", shuffleWriteBytes
        ));
    }


    // 聚合每个 Task 的度量，累加到对应 Stage
    @Override
    public void onTaskEnd(SparkListenerTaskEnd taskEnd) {
        long stageId = taskEnd.stageId();
        long inputBytes = 0L;
        long shuffleReadBytes = 0L;
        long shuffleWriteBytes = 0L;
        TaskMetrics taskMetrics = taskEnd.taskMetrics();
        if (taskMetrics != null) {
            // 获取任务指标数据（inputBytes, shuffleReadBytes, shuffleWriteBytes）
            inputBytes = taskMetrics.inputMetrics().bytesRead();
            shuffleReadBytes = taskMetrics.shuffleReadMetrics().totalBytesRead();
            shuffleWriteBytes = taskMetrics.shuffleWriteMetrics().bytesWritten();
            // 存储阶段任务的指标
            stageInputBytes.put(stageId, stageInputBytes.getOrDefault(stageId, 0L) + inputBytes);
            stageShuffleReadBytes.put(stageId, stageShuffleReadBytes.getOrDefault(stageId, 0L) + shuffleReadBytes);
            stageShuffleWriteBytes.put(stageId, stageShuffleWriteBytes.getOrDefault(stageId, 0L) + shuffleWriteBytes);
        }



    }

}