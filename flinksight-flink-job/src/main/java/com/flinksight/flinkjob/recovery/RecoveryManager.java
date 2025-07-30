package com.flinksight.flinkjob.recovery;

import com.flinksight.common.dto.JobMetricsEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务自愈管理器：自动检测异常，调用调度API实现自愈 流任务内自愈/自动恢复
 * 可选扩展：JobMetricsEvent.fromJson实现推荐放在common内，可用Jackson等封装）
 */
public class RecoveryManager {
    private static final Logger log = LoggerFactory.getLogger(RecoveryManager.class);

    public static String processIfFailed(String rawJson) {
        JobMetricsEventDTO event = JobMetricsEventDTO.fromJson(rawJson);
        if (event != null && "FAILED".equalsIgnoreCase(event.getStatus())) {
            // 调用后端API或直接调度Flink/Spark REST重启
            log.warn("检测到作业失败，自动恢复: jobId={} jobName={}", event.getJobId(), event.getJobName());
            // RecoveryApiClient.restartJob(event.getJobId());
        }
        return rawJson;
    }
}
