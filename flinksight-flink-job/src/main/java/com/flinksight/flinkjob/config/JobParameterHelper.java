package com.flinksight.flinkjob.config;

import org.apache.flink.api.java.utils.ParameterTool;

/**
 * Flink作业运行参数自动适配 Flink作业参数自动化（企业级线上可动态注入参数，适应多集群）
 */
public class JobParameterHelper {

    /**
     * 获取带默认值的参数
     */
    public static String get(ParameterTool params, String key, String defaultValue) {
        return params.has(key) ? params.get(key) : defaultValue;
    }

    /**
     * 强制校验必传参数
     */
    public static String require(ParameterTool params, String key) {
        if (!params.has(key)) throw new IllegalArgumentException("Missing required parameter: " + key);
        return params.get(key);
    }
}
