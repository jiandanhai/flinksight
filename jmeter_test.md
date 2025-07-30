# JMeter 测试说明
1. 使用 JMeter 创建 HTTP POST 请求到 http://localhost:8080/api/metric/batch
2. Body 参考:
[
  {"clusterId":1,"jobId":1,"metricKey":"job_lag","metricValue":123.0,"recordTime":"2025-07-25T20:00:00","tenantId":1}
]
3. 并发线程数建议：100、500、1000 逐步压测
4. 监控系统 QPS、延迟、数据库压力，建议结合 Grafana/Prometheus
