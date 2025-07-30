# 项目目录
    flinksight-flink-job/
    ├── src/
    │   └── main/
    │       └── java/com/flinksight/flinkjob/
    │           ├── FlinkMetricsCollectorJob.java
    │           ├── FlinkAlertStreamJob.java
    │           ├── FlinkJobLauncher.java
    │           └── utils/
    │               └── FlinkJobUtils.java
    ├── pom.xml
    └── README.md
## Flink实时采集/指标/报警流任务

- 典型任务：实时采集Flink/Spark任务指标、报警事件推送、集群/租户/任务多维度隔离
- 所有采集、指标、报警业务与主表结构一致
- 支持Kafka/JDBC/API多源输入与指标推送
- 可用参数灵活配置，批量流任务自动注册与运维
- 代码结构清晰，可一键发布至Yarn/K8S/Standalone等所有主流Flink环境

### 启动命令
flink run -c com.flinksight.flinkjob.FlinkMetricsCollectorJob target/flinksight-flink-job-*.jar --tenantId=1 --clusterId=1 --metricKey=cpu

### 扩展指南
可定制更多采集任务，只需新增Job和对应Source/Sink类
支持JDBC/Kafka/HTTP等多种业务集成，配合主平台自动报警、上报与通知