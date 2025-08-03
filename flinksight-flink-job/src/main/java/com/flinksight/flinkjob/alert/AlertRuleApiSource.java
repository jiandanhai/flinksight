package com.flinksight.flinkjob.alert;

import com.flinksight.common.dto.AlertRuleConfig;
import com.flinksight.common.utils.JsonUtil;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * （平台API动态规则源）
 * 定时拉取报警规则配置（支持动态热更新）
 * 可对接企业级平台配置中心、API网关等
 */
public class AlertRuleApiSource implements SourceFunction<AlertRuleConfig> {
    private final String apiUrl;
    private volatile boolean running = true;

    public AlertRuleApiSource(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Override
    public void run(SourceContext<AlertRuleConfig> ctx) throws Exception {
        long lastVersion = -1;
        while (running) {
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                AlertRuleConfig config = JsonUtil.fromJson(sb.toString(), AlertRuleConfig.class);
                if (config != null && config.getVersion() != lastVersion) {
                    ctx.collect(config);
                    lastVersion = config.getVersion();
                }
            } catch (Exception e) {
                // 生产可打日志、告警
            }
            Thread.sleep(60_000); // 1分钟拉取一次
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
