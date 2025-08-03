package com.flinksight.flinkjob.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.PushGateway;

import java.util.HashMap;
import java.util.Map;

/**
 * Prometheus Pushgateway Sink
 * 全链路监控大屏/Prometheus/企业微信/短信等告警通道适配
 * 用于将作业核心指标流自动推送到Prometheus
 */
public class PrometheusSinkHelper {

    private final PushGateway pushGateway;
    private final CollectorRegistry registry = new CollectorRegistry();

    public PrometheusSinkHelper(String gatewayAddress) {
        this.pushGateway = new PushGateway(gatewayAddress);
    }

    public void pushMetric(String job, double value) throws Exception {
        Map<String, String> groupingKey = new HashMap<>();
        groupingKey.put("job", job);
        // 需实现自定义metric采集、注册
        pushGateway.pushAdd(registry, job, groupingKey);
    }
}
