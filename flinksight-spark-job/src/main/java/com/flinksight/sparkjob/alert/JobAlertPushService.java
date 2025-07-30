package com.flinksight.sparkjob.alert;

import com.flinksight.common.dto.JobMetricsEventDTO;
import com.flinksight.sparkjob.metrics.MetricsReporter;

import java.util.Set;

/**
 * 报警判定与主平台推送（支持多通道）
 * Spark作业异常报警服务，与主平台统一通道对接
 */
public class JobAlertPushService {
    /**
     * 判定并上报异常报警
     */
    public static void checkAndPushAlert(JobMetricsEventDTO event) {
        if ("FAILED".equalsIgnoreCase(event.getStatus()) || "CRITICAL".equalsIgnoreCase(event.getAlertLevel())) {
            event.setAlertMsg("【报警】Spark作业异常：" + event.getJobName());
            event.setAlertLevel("CRITICAL");
            event.setNotifyChannels(Set.of("wechat,email"));
            // 推送报警到主平台
            MetricsReporter.report(event);
        }
    }
}
