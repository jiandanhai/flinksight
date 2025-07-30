package com.flinksight.flinkjob.alert;

import com.flinksight.common.dto.JobMetricsEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务异常判定与报警消息推送 流任务内报警判定与推送
 */
public class JobAlertPushService {
    private static final Logger log = LoggerFactory.getLogger(JobAlertPushService.class);

    /** 判断业务是否需报警 */
    public static boolean shouldAlert(JobMetricsEventDTO event) {
        // 可根据规则库、阈值、租户等扩展
        if (event == null) return false;
        return "FAILED".equalsIgnoreCase(event.getStatus())
                || ("DELAY".equalsIgnoreCase(event.getAlertType()) && event.getLag() != null && event.getLag() > 30000);
    }

    /** 构建报警JSON */
    public static String buildAlertMsg(JobMetricsEventDTO event) {
        // 可扩展：集成钉钉、短信、邮件、Webhook等
        return String.format("{\"jobId\":%d,\"jobName\":\"%s\",\"status\":\"%s\",\"lag\":%.2f,\"alertType\":\"%s\",\"ts\":\"%s\"}",
                event.getJobId(), event.getJobName(), event.getStatus(), event.getLag(), event.getAlertType(), event.getTimestamp());
    }
}
