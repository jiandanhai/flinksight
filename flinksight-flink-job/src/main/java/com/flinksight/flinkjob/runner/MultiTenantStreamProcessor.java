package com.flinksight.flinkjob.runner;

import com.flinksight.common.dto.JobMetricsEventDTO;
import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * 多租户指标流隔离工具
 */
public class MultiTenantStreamProcessor {

    /**
     * 按租户分区处理
     */
    public static DataStream<JobMetricsEventDTO> processByTenant(DataStream<JobMetricsEventDTO> input) {
        // 以tenantId为key分区（Flink KeyBy）
        return input.keyBy(JobMetricsEventDTO::getTenantId)
                .map(event -> {
                    // 可在此实现租户隔离、动态路由、流量整形等
                    return event;
                });
    }
}
