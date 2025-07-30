package com.flinksight.flinkjob.sink;

import com.flinksight.flinkjob.metrics.*;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.http.HttpHost;

import java.util.List;

/**
 * 全场景动态 Sink 工厂
 */
public class DynamicSinkFactory {

    public static void applySink(DataStream<String> stream, String sinkType, String sinkParam, StreamExecutionEnvironment env) {
        switch (sinkType.toLowerCase()) {
            case "kafka":
                stream.sinkTo(MetricsKafkaSinkFactory.build(sinkParam, "job-alert"));
                break;
            case "pulsar":
                // sinkParam: pulsarServiceUrl,topic
                String[] pArr = sinkParam.split(",", 2);
                stream.sinkTo(MetricsPulsarSinkFactory.build(pArr[0], pArr[1]));
                break;
            case "hdfs":
                stream.sinkTo(MetricsHdfsSinkFactory.build(sinkParam));
                break;
            case "hudi":
                // TODO: 如有 Hudi Table API, 可实现对应 Sink
                break;
            case "es":
                String[] arr = sinkParam.split("\\|");
                if (arr.length != 2) throw new IllegalArgumentException("ES参数需为 hosts|index 格式");
                List<HttpHost> hosts = MetricsEsSinkFactory.parseHosts(arr[0]);
                String index = arr[1];
                stream.addSink(MetricsEsSinkFactory.build(hosts, index));
                break;
            default:
                throw new IllegalArgumentException("未知 sink 类型: " + sinkType);
        }
    }
}
