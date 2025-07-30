package com.flinksight.common.utils;

import java.util.UUID;

/**
 * Trace工具类
 * 支持全局唯一TraceId生成、存储与获取
 * 兼容微服务/分布式/流批作业全链路追踪
 */
public class TraceUtil {

    // ThreadLocal保存当前线程的traceId，兼容异步与流式多线程场景
    private static final ThreadLocal<String> traceIdHolder = new ThreadLocal<>();

    /**
     * 生成全局唯一TraceId（可自定义更高性能/更短ID方案）
     */
    public static String generateTraceId() {
        // 可用雪花算法、NanoId等增强唯一性和可读性
        String tid = traceIdHolder.get();
        if (tid == null) {
            tid = UUID.randomUUID().toString();
            traceIdHolder.set(tid);
        }
        return tid;
    }

    /**
     * 设置当前线程的traceId（跨服务/线程传递）
     */
    public static void setCurrentTraceId(String traceId) {
        traceIdHolder.set(traceId);
    }

    /**
     * 获取当前线程的traceId（推荐用于日志链路、审计、埋点等场景）
     */
    public static String getCurrentTraceId() {
        return traceIdHolder.get();
    }

    /**
     * 清理当前线程traceId，防止内存泄漏
     */
    public static void clear() {
        traceIdHolder.remove();
    }

    /**
     * 获取当前traceId，如果没有则自动生成并设置
     */
    public static String getOrCreateTraceId() {
        String traceId = getCurrentTraceId();
        if (traceId == null || traceId.isEmpty()) {
            traceId = generateTraceId();
            setCurrentTraceId(traceId);
        }
        return traceId;
    }
}
