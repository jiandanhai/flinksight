package com.flinksight.flinkjob.diagnose;

import com.flinksight.common.dto.JobMetricsEventDTO;

/**
 * 链路诊断数据落地（全链路流转存库/搜索）
 * 推荐落地 ElasticSearch、Hudi、Kafka、MySQL等，统一API：
 */
public class LinkTraceSink {

    public static void persist(JobMetricsEventDTO event) {
        // 示例写ES/kafka/DB
        // EsSinkHelper.index("diagnose-log", event);
        // KafkaSinkHelper.send("link-trace", event);
    }
}
