# 项目目录
    flinksight-spark-job/
    ├── src/main/java/com/flinksight/sparkjob/
    │   ├── collector/    # 指标采集与SparkListener适配
    │   ├── alert/        # 异常判定/报警推送
    │   ├── metrics/      # 指标整理/外部推送
    │   ├── recovery/     # 任务自动自愈/恢复
    │   ├── sink/         # 通用Sink（Kafka、Pulsar等）
    │   ├── audit/        # 审计日志采集
    │   └── runner/       # 入口（主类/集成模式）
    └── pom.xml


## Spark流批/离线任务

- 典型场景：批量采集历史指标、报警、数据仓库ETL、工单数据归档等
- 结构与Flink一致，所有DTO/VO由common模块统一维护
- 可按生产定时调度或集群任务自动触发

### 启动命令
spark-submit --class com.flinksight.sparkjob.SparkMetricsBatchJob target/flinksight-spark-job-*.jar

### 扩展
支持与主业务MySQL/监控表对接，自动写入或推送至上游
可根据业务需求定制任意批量处理逻辑