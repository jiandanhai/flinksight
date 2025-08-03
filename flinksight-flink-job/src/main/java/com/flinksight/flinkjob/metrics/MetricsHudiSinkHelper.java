package com.flinksight.flinkjob.metrics;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Hudi分区表Sink工具
 */
public class MetricsHudiSinkHelper {

    /**
     * DataStream写入Hudi表（需提前注册Table，推荐在Flink Table API层处理）
     */
    public static void sinkToHudi(DataStream<?> dataStream, StreamTableEnvironment tableEnv, String tableName) {
        // 示例流程：先注册临时表，再SQL写入Hudi分区表
        tableEnv.createTemporaryView("tmp_metrics", dataStream);
        tableEnv.executeSql(
            "INSERT INTO " + tableName + " SELECT * FROM tmp_metrics"
        );
    }
}
