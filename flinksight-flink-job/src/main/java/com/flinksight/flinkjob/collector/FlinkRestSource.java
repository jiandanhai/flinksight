    package com.flinksight.flinkjob.collector;

    import com.flinksight.common.dto.JobMetricsEventDTO;
    import org.apache.flink.api.connector.source.*;
    import org.apache.flink.core.io.SimpleVersionedSerializer;

    import java.util.Objects;

    /**
     * Flink REST 拉取 Job/Vertex 指标，生成你的 JobMetricsEventDTO，写入 Kafka。
     * Flink REST 采集 Source：
     * - 周期性拉取 /jobs/overview → /jobs/{id} → /jobs/{id}/vertices/{vid}/metrics
     * - 组装 JobMetricsEventDTO（字段严格对齐你的项目定义）
     * - lag 计算：若拿到 currentInputWatermark(ms) 且有效，则 now - watermark；否则为空。
     *
     *  轮询流程：
     *    /jobs/overview  →  /jobs/{jid}  →  /jobs/{jid}/vertices/{vid}/metrics?get=k1,k2,k3...
     *   将采集到的关键指标 + 原始 metrics JSON 打入 JobMetricsEventDTO.metricsJson，其他维度字段严格对齐你们的 DTO。
     *
     *   健壮性增强：
     *     - HTTP 超时/重试/UA，cancel() 时会取消正在进行的 HTTP 调用；
     *     - 识别 NaN / Long.MIN_VALUE / 空字符串等“无效水位”，避免错误的 lag；
     *     - 轮询对 cancel 更灵敏（小切片睡眠）。
     */
    public class FlinkRestSource
            implements Source<JobMetricsEventDTO, FlinkRestSplit, FlinkRestEnumState> {

        private final String restUrl;
        private final long tenantId;
        private final String env;
        private final String cluster;
        private final String clusterId;
        private final String clusterType;
        private final long intervalMs;

        public FlinkRestSource(String restUrl, long tenantId, String env,
                               String cluster, String clusterId, String clusterType,
                               long intervalMs) {
            this.restUrl = Objects.requireNonNull(
                    restUrl.endsWith("/") ? restUrl.substring(0, restUrl.length() - 1) : restUrl);
            this.tenantId = tenantId;
            this.env = env;
            this.cluster = cluster;
            this.clusterId = clusterId;
            this.clusterType = clusterType;
            this.intervalMs = intervalMs;
        }

        @Override
        public Boundedness getBoundedness() {
            return Boundedness.CONTINUOUS_UNBOUNDED;
        }

        @Override
        public SourceReader<JobMetricsEventDTO, FlinkRestSplit> createReader(SourceReaderContext ctx) {
            return new FlinkRestSourceReader(ctx, intervalMs);
        }

        @Override
        public SplitEnumerator<FlinkRestSplit, FlinkRestEnumState> createEnumerator(
                SplitEnumeratorContext<FlinkRestSplit> ctx) {
            // 单 split（携带你所有上下文），分配给第一个 reader
            return new FlinkRestEnumerator(
                    ctx,
                    new FlinkRestSplit("rest-0", restUrl, tenantId, env, cluster, clusterId, clusterType),
                    false
            );
        }

        @Override
        public SplitEnumerator<FlinkRestSplit, FlinkRestEnumState> restoreEnumerator(
                SplitEnumeratorContext<FlinkRestSplit> ctx, FlinkRestEnumState checkpoint) {
            return new FlinkRestEnumerator(
                    ctx,
                    new FlinkRestSplit("rest-0", restUrl, tenantId, env, cluster, clusterId, clusterType),
                    checkpoint != null && checkpoint.assigned()
            );
        }

        @Override
        public SimpleVersionedSerializer<FlinkRestSplit> getSplitSerializer() {
            return new FlinkRestSplitSerializer();
        }

        @Override
        public SimpleVersionedSerializer<FlinkRestEnumState> getEnumeratorCheckpointSerializer() {
            return new FlinkRestEnumStateSerializer();
        }
    }