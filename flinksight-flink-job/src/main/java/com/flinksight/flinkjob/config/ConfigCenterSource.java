package com.flinksight.flinkjob.config;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * 平台配置中心（Nacos/Apollo）动态参数自动热加载
 * 配置中心动态参数 Source 实现：
 * 平台参数如“报警规则URL、sink类型、阈值”等可在平台动态变更，作业流内热加载，无需重启
 */
public class ConfigCenterSource implements SourceFunction<String> {
    private final String configKey;
    private volatile boolean running = true;

    public ConfigCenterSource(String configKey) { this.configKey = configKey; }

    @Override
    public void run(SourceContext<String> ctx) throws Exception {
        while (running) {
            String value = ConfigCenterClient.get(configKey); // 支持Nacos/Apollo等
            ctx.collect(value);
            Thread.sleep(60_000);
        }
    }
    @Override public void cancel() { running = false; }
}
