package com.flinksight.flinkjob.config;

import org.apache.flink.api.java.utils.ParameterTool;

/**
 * 多环境适配与动态上下文参数注入
 * 动态上下文参数注入（如环境标签、租户、作业优先级等）
 */
public class DynamicContextHelper {

    public static String getEnvLabel(ParameterTool params) {
        return params.get("env.label", "prod");
    }

    public static Long getTenantId(ParameterTool params) {
        return params.has("tenant.id") ? params.getLong("tenant.id") : -1L;
    }

    public static String getJobPriority(ParameterTool params) {
        return params.get("job.priority", "normal");
    }
}
