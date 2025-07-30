package com.flinksight.flinkjob.metrics;

import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * ElasticSearch Sink 工厂
 */
public class MetricsEsSinkFactory {

    public static ElasticsearchSink<String> build(List<HttpHost> hosts, String index) {
        return new ElasticsearchSink.Builder<String>(
                hosts,
                (element, ctx, indexer) -> indexer.add(createIndexRequest(index, element))
        ).build();
    }

    public static List<HttpHost> parseHosts(String hostsStr) {
        return Arrays.stream(hostsStr.split(","))
                .map(h -> {
                    String[] hp = h.split(":");
                    return new HttpHost(hp[0], Integer.parseInt(hp[1]), "http");
                }).toList();
    }

    private static IndexRequest createIndexRequest(String index, String element) {
        Map<String, Object> json = new HashMap<>();
        json.put("event", element);
        return Requests.indexRequest().index(index).source(json);
    }
}
