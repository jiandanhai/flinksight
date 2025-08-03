package com.flinksight.flinkjob.sink;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 企业微信告警Sink
 */
public class WeChatAlertSink implements SinkFunction<String> {
    private final String apiUrl;

    public WeChatAlertSink(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Override
    public void invoke(String value, Context context) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(value.getBytes());
            os.flush();
            os.close();
            conn.getResponseCode();
        } catch (Exception e) {
            // 生产建议写DLQ
        }
    }
}
