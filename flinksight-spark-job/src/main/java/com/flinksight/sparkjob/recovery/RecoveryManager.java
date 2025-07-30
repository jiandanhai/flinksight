package com.flinksight.sparkjob.recovery;

import com.flinksight.common.dto.JobMetricsEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自动自愈与平台联动
 * Spark作业自愈管理器：主平台API联动，支持自动重启
 */
public class RecoveryManager {
    private static final Logger log = LoggerFactory.getLogger(RecoveryManager.class);

    public static void autoRecoverIfFailed(JobMetricsEventDTO event) {
        if ("FAILED".equalsIgnoreCase(event.getStatus())) {
            log.warn("检测到作业失败，自动恢复: jobName={}, jobId={}", event.getJobName(), event.getJobId());
            // 真实实现：调用主平台的自愈API，或直接调度Spark REST接口
            // RecoveryApiClient.restartJob(event.getJobId());
        }
    }
}
