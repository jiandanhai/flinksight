package com.flinksight.flinkjob.recovery;

import com.flinksight.common.dto.JobMetricsEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 企业自定义自动恢复策略引擎（策略可动态配置）
 */
public class CustomRecoveryStrategy {

    private static final Logger log = LoggerFactory.getLogger(CustomRecoveryStrategy.class);

    public static void recover(JobMetricsEventDTO event) {
        if ("FAILED".equalsIgnoreCase(event.getStatus())) {
            if ("HIGH".equals(event.getJobPriority())) {
                // 高优先级作业自动重启并通知运维
                log.warn("高优先级作业自动重启: {}", event.getJobId());
                // RecoveryApiClient.restartJob(event.getJobId());
            } else {
                // 低优先级可转人工
                log.info("低优先级作业失败: {}", event.getJobId());
            }
        }
        // 可动态支持“自动扩容/降级/转备份/发短信等”
    }
}
