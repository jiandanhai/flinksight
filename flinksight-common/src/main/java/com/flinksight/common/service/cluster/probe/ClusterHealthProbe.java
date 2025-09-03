// src/main/java/com/flinksight/backend/service/cluster/probe/ClusterHealthProbe.java
package com.flinksight.common.service.cluster.probe;


import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.dto.ClusterHealthDTO;

/**
 * 集群健康探针（可插拔）
 */
public interface ClusterHealthProbe {
  /** 对给定集群执行健康检查与关键指标采样 */
  ClusterHealthDTO check(ClusterDTO c) throws Exception;

  /** 是否支持该 Cluster 类型（例如 FLINK / SPARK / YARN / K8S） */
  boolean supports(String type);
}
