package com.flinksight.sparkjob.audit;

import com.flinksight.common.dto.AuditLogDTO;
import com.flinksight.common.service.AuditLogWriter;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 非Spring环境下的Rest实现，用于作业自动注册/配置变更审计日志打回后台
 * 用于 Spark/Flink 等非 Spring 环境，把审计日志通过 HTTP POST 到后端 flinksight-backend 服务。
 */
public class RestAuditLogServiceImpl implements AuditLogWriter {

    private final String backendUrl; // 如：http://backend:8080/api/audit/log

    public RestAuditLogServiceImpl(String backendUrl) {
        this.backendUrl = backendUrl.endsWith("/") ? backendUrl : backendUrl + "/";
    }

    @Override
    public AuditLogDTO createAuditLog(AuditLogDTO log) {
        // 如确实需要通用 create，也可实现；否则抛 Unsupported 即可
        throw new UnsupportedOperationException("create not supported in this client, use logConfigChange");
    }


    @Override
    public void logConfigChange(String configType, String dataId, Long tenantId, String operator, String config, String traceId) {
        try {
            URL url = new URL(backendUrl + "api/audit/log/config-change");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            // 构造JSON
            String body = String.format(
                    "{\"configType\":\"%s\",\"dataId\":\"%s\",\"tenantId\":%d,\"operator\":\"%s\",\"config\":%s,\"traceId\":\"%s\"}",
                    configType, dataId, tenantId, operator, escape(config), traceId
            );
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes("UTF-8"));
            }
            int code = conn.getResponseCode();
            if (code != 200 && code != 201) {
                System.err.println("AuditLog REST上报失败: " + code);
            }
        } catch (Exception e) {
            System.err.println("AuditLog REST上报异常: " + e.getMessage());
        }
    }

    private String escape(String str) {
        return str == null ? "" : str.replace("\"", "\\\"");
    }


}
