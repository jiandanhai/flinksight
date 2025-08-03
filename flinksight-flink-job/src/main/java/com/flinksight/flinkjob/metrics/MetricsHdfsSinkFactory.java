package com.flinksight.flinkjob.metrics;

import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;

/**
 * 企业级 HDFS Sink 工厂
 */
public class MetricsHdfsSinkFactory {
    public static FileSink<String> build(String hdfsPath) {
        return FileSink.forRowFormat(new Path(hdfsPath), new SimpleStringEncoder<String>("UTF-8"))
                .build();
    }
}
