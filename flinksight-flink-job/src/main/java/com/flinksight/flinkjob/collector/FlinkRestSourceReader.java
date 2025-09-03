package com.flinksight.flinkjob.collector;

import com.fasterxml.jackson.databind.JsonNode;
import com.flinksight.common.dto.JobMetricsEventDTO;
import com.flinksight.common.utils.JobIdCodec;
import com.flinksight.common.utils.Jsons;
import com.flinksight.common.utils.TimeUtil;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.flink.api.connector.source.ReaderOutput;
import org.apache.flink.api.connector.source.SourceReader;
import org.apache.flink.api.connector.source.SourceReaderContext;
import org.apache.flink.core.io.InputStatus;

import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 后台定时轮询 Flink REST，把生成的 DTO 放入队列，pollNext 持续吐出。
 * 完全保留你在 RichSourceFunction 里的采集/解析/lag 计算/HTTP 超时与取消行为。
 */
public class FlinkRestSourceReader implements SourceReader<JobMetricsEventDTO, FlinkRestSplit> {

    private final SourceReaderContext ctx;
    private final long intervalMs;

    private final Queue<JobMetricsEventDTO> buffer = new ConcurrentLinkedQueue<>();
    private final AtomicReference<CompletableFuture<Void>> available =
            new AtomicReference<>(new CompletableFuture<>());

    private final ScheduledExecutorService exec =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "flinksight-flink-rest-poller"); t.setDaemon(true); return t;
            });

    private volatile boolean closed = false;
    private volatile FlinkRestSplit split;
    private volatile Call inFlight;

    // —— 你的 HTTP 与 JSON 工具（同原代码）——
    private static final OkHttpClient HTTP = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(6, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    /** 你的指标清单（不丢失） */
    private static final List<String> METRIC_KEYS = Arrays.asList(
            "currentInputWatermark",
            "numRecordsInPerSecond",
            "numRecordsOutPerSecond",
            "busyTimeMsPerSecond",
            "backPressuredTimeMsPerSecond"
    );

    public FlinkRestSourceReader(SourceReaderContext ctx, long intervalMs) {
        this.ctx = ctx;
        this.intervalMs = Math.max(1000L, intervalMs);
    }

    @Override public void start() { /* 在 addSplits 时启动 */ }

    @Override
    public void addSplits(List<FlinkRestSplit> splits) {
        if (splits == null || splits.isEmpty()) return;
        this.split = splits.get(0);
        exec.scheduleWithFixedDelay(this::safePollOnce, 0L, intervalMs, TimeUnit.MILLISECONDS);
    }

    @Override public void notifyNoMoreSplits() { /* no-op */ }

    @Override
    public CompletableFuture<Void> isAvailable() {
        return available.get();
    }


    @Override
    public InputStatus pollNext(ReaderOutput<JobMetricsEventDTO> out) throws Exception {
        JobMetricsEventDTO e = buffer.poll();

        if (e == null) {
            // 队列空：重置 future，让框架等待 isAvailable() 完成后再调一次
            available.compareAndSet(available.get(), new java.util.concurrent.CompletableFuture<>());
            return InputStatus.NOTHING_AVAILABLE;
        }

        // 一次性把当前可用的都吐出
        int emitted = 0;
        while (e != null) {
            out.collect(e);
            emitted++;
            e = buffer.poll();
        }

        // 已经把缓冲清空，当前没有更多数据可读
        return InputStatus.NOTHING_AVAILABLE;
    }

    @Override
    public List<FlinkRestSplit> snapshotState(long checkpointId) {
        return split == null ? Collections.emptyList() : Collections.singletonList(split);
    }

    @Override
    public void close() {
        closed = true;
        Call c = inFlight; if (c != null) try { c.cancel(); } catch (Throwable ignore) {}
        exec.shutdownNow();
    }

    // ------------- 内部逻辑（与你原代码一致） -------------

    private void safePollOnce() {
        if (closed || split == null) return;
        try {
            List<JobMetricsEventDTO> list = fetchOnce(split);
            if (!list.isEmpty()) {
                buffer.addAll(list);
                CompletableFuture<Void> f = available.get();
                f.complete(null); // 唤醒
            }
        } catch (SocketTimeoutException ignore) {
        } catch (Throwable ignore) {
        }
    }

    private List<JobMetricsEventDTO> fetchOnce(FlinkRestSplit s) throws Exception {
        List<JobMetricsEventDTO> out = new ArrayList<>();
        long now = System.currentTimeMillis();

        JsonNode jobs = getJson(s.restUrl + "/jobs/overview").path("jobs");
        if (jobs == null || !jobs.isArray()) return out;

        for (JsonNode j : jobs) {
            if (closed) break;

            final String jidHex = j.path("jid").asText(null);
            if (jidHex == null || jidHex.isEmpty()) continue;

            Long jobId = safeJobId(jidHex);
            final String jobName = j.path("name").asText("unknown");
            final String state   = j.path("state").asText(null);

            JsonNode detail = getJson(s.restUrl + "/jobs/" + jidHex);
            JsonNode vertices = detail.path("vertices");
            if (vertices == null || !vertices.isArray()) continue;

            final String getParam = String.join(",", METRIC_KEYS);

            for (JsonNode v : vertices) {
                if (closed) break;

                final String vid = v.path("id").asText(null);
                if (vid == null) continue;

                String mUrl = s.restUrl + "/jobs/" + jidHex + "/vertices/" + vid + "/metrics?get=" + getParam;
                JsonNode metrics = getJson(mUrl);

                Map<String, Object> metricMap = new LinkedHashMap<>(METRIC_KEYS.size() + 2);
                Double lagMs = null;

                if (metrics != null && metrics.isArray()) {
                    for (JsonNode m : metrics) {
                        final String k  = m.path("id").asText();
                        final String vs = m.path("value").asText(); // 可能为 NaN / "" / Long.MIN_VALUE / "0"
                        metricMap.put(k, vs);

                        if ("currentInputWatermark".equals(k)) {
                            Long wm = parseWatermarkMillis(vs);
                            if (wm != null && wm > 0L && wm < now + 3_600_000L) {
                                lagMs = (double)Math.max(0L, now - wm);
                            }
                        }
                    }
                }

                JobMetricsEventDTO e = JobMetricsEventDTO.builder()
                        .jobId(jobId)
                        .jobName(jobName)
                        .vertexId(vid)
                        .tenantId(s.tenantId)
                        .env(s.env)
                        .engine("FLINK")
                        .cluster(s.cluster)
                        .clusterId(s.clusterId)
                        .clusterType(s.clusterType)
                        .status(state)
                        .lag(lagMs)
                        .timestamp(TimeUtil.nowIso())
                        .metricsJson(Jsons.to(metricMap))
                        .build();

                out.add(e);
            }
        }
        return out;
    }

    private JsonNode getJson(String url) throws Exception {
        Request req = new Request.Builder()
                .url(url).get()
                .header("Accept", "application/json;charset=UTF-8")
                .header("User-Agent", "flinksight-metrics-collector/1.0")
                .build();

        Call call = HTTP.newCall(req);
        inFlight = call;
        try (Response resp = call.execute()) {
            if (!resp.isSuccessful()) throw new RuntimeException("HTTP " + resp.code() + " : " + url);
            String body = Objects.requireNonNull(resp.body(), "empty response").string();
            if (body.isEmpty()) return Jsons.object();
            return Jsons.readTree(body.getBytes(StandardCharsets.UTF_8));
        } finally {
            inFlight = null;
        }
    }

    private static Long safeJobId(String jidHex) {
        try {
            Long v = JobIdCodec.toLongOrNull(jidHex);
            if (v != null) return v;
        } catch (Throwable ignore) { }
        return (long) (jidHex.hashCode() & 0x7fffffff);
    }

    private static Long parseWatermarkMillis(String raw) {
        if (raw == null || raw.isEmpty()) return null;
        if ("NaN".equalsIgnoreCase(raw) || "-9223372036854775808".equals(raw)) return null;
        try { return Long.parseLong(raw.trim()); } catch (NumberFormatException e) { return null; }
    }
}
