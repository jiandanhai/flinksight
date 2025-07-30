package com.flinksight.common.service;

import com.flinksight.common.dto.ClusterDTO;

import java.util.List;

/**
 * 集群自动发现与元数据采集服务接口
 * 支持YARN、K8S、Standalone等多环境集群发现与在线状态探测
 */
public interface ClusterDiscoveryService {

    /**
     * 自动发现已配置集群的状态和元数据
     * @param configs 已注册/配置的集群DTO列表
     * @return 带最新状态的集群DTO列表
     */
    List<ClusterDTO> discoverClusters(List<ClusterDTO> configs);

    /**
     * 探测单个集群的状态
     * @param config 集群DTO
     * @return 刷新后状态的集群DTO
     */
    ClusterDTO discoverCluster(ClusterDTO config);

}
