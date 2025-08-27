package com.flinksight.common.service;

import com.flinksight.common.dto.*;
import com.flinksight.common.model.PageResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 集群业务接口
 * Cluster Service
 */
public interface ClusterService extends SoftDeleteService<ClusterDTO, Long>  {
    Optional<ClusterDTO> getClusterById(Long clusterId);

    PageResult<ClusterDTO> list(int page, int size);

    void batchAddNodes(BatchAddNodesRequestDTO req);

    // Cluster
    ClusterDTO createCluster(ClusterDTO req);

    ClusterDTO updateCluster(Long id, ClusterDTO req);

    ClusterDTO setClusterEnable(Long id, boolean enable);

    void setClusterEnableBatch(List<Long> ids, boolean enable);

    // Node
    NodeDTO createNode(NodeDTO req);

    List<NodeDTO> batchAddNodes(List<NodeDTO> reqs);

    NodeDTO setNodeEnable(Long nodeId, boolean enable);

    void setNodeEnableBatch(List<Long> nodeIds, boolean enable);

    PageResult<NodeDTO> getNodesByCluster(Long clusterId, int page, int size);

    // Health & Metrics
    PageResult<NodeHealthDTO> getNodeHealth(Long nodeId, int page, int size);

    NodeMetricDTO getNodeMetric(Long clusterId, LocalDateTime from, LocalDateTime to);
}
