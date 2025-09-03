package com.flinksight.flinkjob.collector;

import org.apache.flink.api.connector.source.SplitEnumerator;
import org.apache.flink.api.connector.source.SplitEnumeratorContext;
import org.apache.flink.api.connector.source.SplitsAssignment;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单 Split 的枚举器：
 * - 将携带 REST 上下文的 FlinkRestSplit 分配给第一个可用的 reader；
 * - 支持从失败的 reader 回收后重新分配；
 * - Checkpoint 仅记录是否已分配（FlinkRestEnumState.assigned）。
 */
public class FlinkRestEnumerator implements SplitEnumerator<FlinkRestSplit, FlinkRestEnumState> {

    private final SplitEnumeratorContext<FlinkRestSplit> context;
    private final FlinkRestSplit split;
    private boolean assigned; // 是否已经分配给某个 subtask

    public FlinkRestEnumerator(SplitEnumeratorContext<FlinkRestSplit> context,
                               FlinkRestSplit split,
                               boolean assigned) {
        this.context = context;
        this.split = split;
        this.assigned = assigned;
    }

    @Override
    public void start() {
        // no-op：等待 reader 上线后触发 addReader/handleSplitRequest
    }

    @Override
    public void handleSplitRequest(int subtaskId, String requesterHostname) {
        assignIfNeeded(subtaskId);
    }

    @Override
    public void addReader(int subtaskId) {
        assignIfNeeded(subtaskId);
    }

    @Override
    public void addSplitsBack(List<FlinkRestSplit> splits, int subtaskId) {
        // 有 reader 失败或取消时会回收 split，这里标记为未分配，待下次有 reader 再分配
        if (splits != null && !splits.isEmpty()) {
            assigned = false;
        }
    }

    @Override
    public FlinkRestEnumState snapshotState(long checkpointId) throws Exception {
        return new FlinkRestEnumState(assigned);
    }

    @Override
    public void close() throws IOException {
        // no-op
    }

    // ---------------- helpers ----------------

    private void assignIfNeeded(int subtaskId) {
        if (assigned) return;
        Map<Integer, List<FlinkRestSplit>> assignment = new HashMap<>();
        assignment.put(subtaskId, Collections.singletonList(split));
        context.assignSplits(new SplitsAssignment<>(assignment));
        context.signalNoMoreSplits(subtaskId);
        assigned = true;
    }
}
