package com.flinksight.flinkjob.alert;

import com.flinksight.common.dto.AlertRuleConfig;
import com.flinksight.common.utils.Jsons;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

/**
 * 周期拉取 AlertRuleConfig(JSON)：仅当 version 变大时才广播。
 */
public class AlertRuleApiSourceV2 extends RichSourceFunction<AlertRuleConfig> {
  private final String api; private final long intervalMs; private volatile boolean running = true;
  private final OkHttpClient http = new OkHttpClient();
  private long lastVersion = -1L;

  public AlertRuleApiSourceV2(String api){ this(api, 30_000); }
  public AlertRuleApiSourceV2(String api, long intervalMs){ this.api=api; this.intervalMs=intervalMs; }

  @Override public void run(SourceContext<AlertRuleConfig> ctx) throws Exception {
    while (running){
      try {
        Request req = new Request.Builder().url(api).get().build();
        try (Response resp = http.newCall(req).execute()){
          if (resp.isSuccessful() && resp.body()!=null){
            AlertRuleConfig cfg = Jsons.from(resp.body().string(), AlertRuleConfig.class);
            if (cfg!=null && cfg.getVersion() > lastVersion){
              synchronized (ctx.getCheckpointLock()){ ctx.collect(cfg); }
              lastVersion = cfg.getVersion();
            }
          }
        }
      } catch (Exception ignore) {}
      Thread.sleep(intervalMs);
    }
  }
  @Override public void cancel(){ running = false; }
}