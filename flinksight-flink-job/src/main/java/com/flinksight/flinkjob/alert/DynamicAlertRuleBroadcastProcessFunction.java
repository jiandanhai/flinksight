package com.flinksight.flinkjob.alert;

import com.flinksight.common.dto.AlertRuleConfig;
import com.flinksight.common.dto.JobMetricsEventDTO;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

/**
 * （动态报警规则应用）
 * 动态报警规则与事件流广播连接处理器
 * 可按租户/作业动态判定报警，支持平台热更新
 */
public class DynamicAlertRuleBroadcastProcessFunction extends BroadcastProcessFunction<JobMetricsEventDTO, AlertRuleConfig, JobMetricsEventDTO> {
    public static final MapStateDescriptor<String, AlertRuleConfig.JobAlertRule> RULES_DESC =
            new MapStateDescriptor<>("rules", String.class, AlertRuleConfig.JobAlertRule.class);

    @Override
    public void processElement(JobMetricsEventDTO event, ReadOnlyContext ctx, Collector<JobMetricsEventDTO> out) throws Exception {
        AlertRuleConfig.JobAlertRule rule = ctx.getBroadcastState(RULES_DESC).get(String.valueOf(event.getJobId()));
        // 动态判定: 只有符合规则才报警
        if (rule != null && event.getLag() != null && rule.getLagThreshold() != null && event.getLag() > rule.getLagThreshold()) {
            event.setAlertType("DELAY");
            event.setAlertLevel(rule.getAlertLevel());
            event.setNotifyChannels(rule.getNotifyChannels());
            out.collect(event);
        }
        // 其它规则可扩展：如FAILED等状态报警等
    }

    @Override
    public void processBroadcastElement(AlertRuleConfig config, Context ctx, Collector<JobMetricsEventDTO> out) throws Exception {
        if (config != null && config.getJobRules() != null) {
            for (var entry : config.getJobRules().entrySet()) {
                ctx.getBroadcastState(RULES_DESC).put(String.valueOf(entry.getKey()), entry.getValue());
            }
        }
    }
}
