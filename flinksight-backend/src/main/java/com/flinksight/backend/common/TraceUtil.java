package com.flinksight.backend.common;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * Trace工具类，获取/生成本次请求的traceId。
 * 支持：自动注入、线程隔离、日志MDC兼容
 */
public class TraceUtil {

    // MDC中的traceId key，日志系统会自动写入
    public static final String TRACE_ID_KEY = "traceId";
    // ThreadLocal保证每个请求独立
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    /**
     * 获取当前请求的traceId，自动生成
     * @return traceId
     */
    public static String getCurrentTraceId() {
        String traceId = CONTEXT.get();
        if (traceId == null) {
            // 优先从MDC获取（兼容日志链路）
            traceId = MDC.get(TRACE_ID_KEY);
            if (traceId == null) {
                // 没有就新生成
                traceId = UUID.randomUUID().toString().replace("-", "");
                setTraceId(traceId);
            }
        }
        return traceId;
    }

    /**
     * 主动设置traceId（如接入分布式链路追踪中间件可用）
     */
    public static void setTraceId(String traceId) {
        CONTEXT.set(traceId);
        MDC.put(TRACE_ID_KEY, traceId); // 日志自动输出traceId
    }

    /**
     * 清理（请求结束时，防内存泄漏）
     */
    public static void clear() {
        CONTEXT.remove();
        MDC.remove(TRACE_ID_KEY);
    }
}
