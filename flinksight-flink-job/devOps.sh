#!/bin/bash
# flinksight-flink-job 启动脚本
FLINK_HOME=/opt/flink
JAR_PATH=/data/jars/flinksight-flink-job.jar
MAIN_CLASS=com.flinksight.flinkjob.runner.FlinkOpsJob
PARAMS="--kafka.bootstrap.servers=kafka:9092 --job.metrics.topic=job-metrics --sink.type=kafka --job.name=ops-job-prod"
$FLINK_HOME/bin/flink run -c $MAIN_CLASS $JAR_PATH $PARAMS
