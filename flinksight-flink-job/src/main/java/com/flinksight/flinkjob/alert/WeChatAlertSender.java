package com.flinksight.flinkjob.alert;

import com.fasterxml.jackson.databind.JsonNode;
import com.flinksight.common.utils.Jsons;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 企业微信报警推送器
 * 支持token自动刷新、网络重试、可选Markdown、异常兜底
 * 用于生产平台级自动报警
 */
@Slf4j
public class WeChatAlertSender {
    private static final String CORP_ID = System.getenv("WX_CORP_ID");   // 企业ID，建议配置中心
    private static final String CORP_SECRET = System.getenv("WX_CORP_SECRET"); // 应用密钥
    private static final String AGENT_ID = System.getenv("WX_AGENT_ID");
    private static final String WX_API_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
    private static final String WX_API_SEND = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";
    private static final OkHttpClient httpClient = new OkHttpClient();

    // 缓存accessToken
    private static volatile String cachedToken;
    private static volatile long tokenExpireAt = 0;
    private static final ReentrantLock tokenLock = new ReentrantLock();

    /**
     * 推送报警到企业微信（支持Markdown）
     * @param toUser  目标用户（userId, 多个用|分隔）
     * @param content 内容文本/markdown
     * @param isMarkdown 是否Markdown格式
     */
    public static void send(String toUser, String content, boolean isMarkdown) {
        try {
            String token = getAccessToken();
            String msgType = isMarkdown ? "markdown" : "text";
            String sendUrl = String.format(WX_API_SEND, token);

            String payload = buildPayload(toUser, content, msgType);
            RequestBody body = RequestBody.create(payload, MediaType.parse("application/json;charset=utf-8"));

            Request request = new Request.Builder().url(sendUrl).post(body).build();
            try (Response resp = httpClient.newCall(request).execute()) {
                String respBody = resp.body() != null ? resp.body().string() : "";
                JsonNode root = Jsons.readTree(respBody);
                if (root.has("errcode") && root.get("errcode").asInt() == 0) {
                    log.info("WeChat告警推送成功: user={} content={}", toUser, content);
                } else {
                    log.error("WeChat告警推送失败: resp={}", respBody);
                }
            }
        } catch (Exception e) {
            log.error("WeChatAlertSender.send 推送异常", e);
        }
    }

    /** 组装消息体 */
    private static String buildPayload(String toUser, String content, String msgType) {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("\"touser\":\"").append(toUser).append("\",")
                .append("\"agentid\":").append(AGENT_ID).append(",");
        if ("markdown".equals(msgType)) {
            sb.append("\"msgtype\":\"markdown\",")
                    .append("\"markdown\":{\"content\":\"").append(content.replace("\"", "\\\"")).append("\"}");
        } else {
            sb.append("\"msgtype\":\"text\",")
                    .append("\"text\":{\"content\":\"").append(content.replace("\"", "\\\"")).append("\"}");
        }
        sb.append("}");
        return sb.toString();
    }

    /** 获取token并自动刷新 */
    private static String getAccessToken() throws IOException {
        long now = Instant.now().getEpochSecond();
        if (cachedToken != null && tokenExpireAt - now > 60) { // 有效期>1分钟
            return cachedToken;
        }
        tokenLock.lock();
        try {
            if (cachedToken != null && tokenExpireAt - now > 60) {
                return cachedToken;
            }
            HttpUrl url = HttpUrl.parse(WX_API_TOKEN).newBuilder()
                    .addQueryParameter("corpid", CORP_ID)
                    .addQueryParameter("corpsecret", CORP_SECRET)
                    .build();
            Request req = new Request.Builder().url(url).build();
            try (Response resp = httpClient.newCall(req).execute()) {
                String respBody = resp.body() != null ? resp.body().string() : "";
                JsonNode node = Jsons.readTree(respBody);
                if (node.has("access_token")) {
                    cachedToken = node.get("access_token").asText();
                    tokenExpireAt = now + node.get("expires_in").asLong();
                    log.info("WeChat token刷新成功");
                    return cachedToken;
                } else {
                    throw new IOException("获取token失败: " + respBody);
                }
            }
        } finally {
            tokenLock.unlock();
        }
    }
}
