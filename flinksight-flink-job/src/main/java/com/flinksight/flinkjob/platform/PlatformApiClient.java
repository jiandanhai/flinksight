package com.flinksight.flinkjob.platform;

import com.flinksight.common.dto.JobMetricsEventDTO;
import com.flinksight.common.utils.JsonUtil;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

/**
 * 与平台后端API集成，动态下发配置、上报诊断/链路日志(与平台API自动集成（如配置下发、事件落地、结果反馈）)
 * 可用于“自动推送诊断日志、报警事件、恢复反馈”等
 */
public class PlatformApiClient {
    public static void reportEvent(JobMetricsEventDTO event, String apiUrl) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(2000);
            conn.setRequestProperty("Content-Type", "application/json");
            String json = JsonUtil.toJson(event);
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();
            conn.getResponseCode(); // 可处理响应
        } catch (Exception e) {
            // 重试/告警
        }
    }
}
