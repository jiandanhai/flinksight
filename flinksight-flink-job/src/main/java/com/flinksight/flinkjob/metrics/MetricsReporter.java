package com.flinksight.flinkjob.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 指标采集与上报Prometheus/自研监控   流任务内指标采集/推送
 */
public class MetricsReporter {
    private static final Logger log = LoggerFactory.getLogger(MetricsReporter.class);

    public static void reportMetrics(String rawJson) {
        // 可扩展：Prometheus PushGateway/自研监控
        log.info("上报指标: {}", rawJson);
    }
}
