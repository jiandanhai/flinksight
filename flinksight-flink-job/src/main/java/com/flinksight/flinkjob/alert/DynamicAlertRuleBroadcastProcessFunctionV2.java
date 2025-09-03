package com.flinksight.flinkjob.alert;

import com.flinksight.common.dto.AlertRuleConfig;
import com.flinksight.common.dto.AlertRuleConfig.JobAlertRule;
import com.flinksight.common.dto.JobMetricsEventDTO;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

import java.util.Map;

/**
 * 规则广播处理（与你的结构对齐）：
 * - BroadcastState<Long, JobAlertRule> 保存 jobId→规则；
 * - 收到更高 version 的配置时，**全量覆盖**当前规则；
 * - 事件到达时，优先 jobId 精确匹配；否则尝试默认规则 key=0；
 * - 命中逻辑：若 rule.lagThreshold 存在且 e.lag(毫秒) > 阈值，则触发；
 * - 命中后回填：alertType、alertLevel、notifyChannels、autoRecover、alertMsg。
 */
public class DynamicAlertRuleBroadcastProcessFunctionV2
  extends BroadcastProcessFunction<JobMetricsEventDTO, AlertRuleConfig, JobMetricsEventDTO> {

  public static final MapStateDescriptor<Long, JobAlertRule> JOB_RULES_DESC =
      new MapStateDescriptor<>("job_rules", Types.LONG, Types.POJO(JobAlertRule.class));
  public static final MapStateDescriptor<String, Long> VERSION_DESC =
      new MapStateDescriptor<>("rules_version", Types.STRING, Types.LONG);

  @Override public void processBroadcastElement(AlertRuleConfig cfg, Context ctx, Collector<JobMetricsEventDTO> out) throws Exception {
    BroadcastState<String, Long> vState = ctx.getBroadcastState(VERSION_DESC);
    Long current = vState.get("version");
    if (current != null && cfg.getVersion() <= current) return; // 旧版本丢弃

    BroadcastState<Long, JobAlertRule> rules = ctx.getBroadcastState(JOB_RULES_DESC);
    rules.clear();
    if (cfg.getJobRules()!=null){
      for (Map.Entry<Long, JobAlertRule> e : cfg.getJobRules().entrySet()){
        rules.put(e.getKey(), e.getValue());
      }
    }
    vState.put("version", cfg.getVersion());
  }

  @Override public void processElement(JobMetricsEventDTO ev, ReadOnlyContext ctx, Collector<JobMetricsEventDTO> out) throws Exception {
    ReadOnlyBroadcastState<Long, JobAlertRule> rs = ctx.getBroadcastState(JOB_RULES_DESC);
    if (ev.getJobId()==null) return;

    JobAlertRule rule = rs.get(ev.getJobId());
    if (rule == null) rule = rs.get(0L); // 默认规则
    if (rule == null) return;

    Double th = rule.getLagThreshold();
    Double lag = ev.getLag();
    if (th != null && lag != null && lag > th) {
      // 命中，补齐告警字段（沿用你的 DTO 字段名）
      ev.setAlertType("LAG_OVER_THRESHOLD");
      ev.setAlertLevel(rule.getAlertLevel());
      ev.setNotifyChannels(rule.getNotifyChannels());
      ev.setAutoRecover(rule.isEnableAutoRecover());
      ev.setAlertMsg("lag="+lag.longValue()+"ms > threshold="+th.longValue()+"ms");
      out.collect(ev);
    }
  }
}