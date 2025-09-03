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
    Optional<ClusterDTO> get(Long clusterId);

    PageResult<ClusterDTO> list(String type, Integer status, String q, int page, int size);

    void batchAddNodes(BatchAddNodesRequestDTO req);

    ClusterDTO update(Long id, ClusterDTO req);

    void changeStatus(Long id, Integer status);

    ClusterHealthDTO healthById(Long id);

    // Node
    NodeDTO createNode(NodeDTO req);

    List<NodeDTO> batchAddNodes(List<NodeDTO> reqs);

    NodeDTO setNodeEnable(Long nodeId, boolean enable);

    void setNodeEnableBatch(List<Long> nodeIds, boolean enable);

    PageResult<NodeDTO> getNodesByCluster(Long clusterId, int page, int size);

    // Health & Metrics
    PageResult<NodeHealthDTO> listNodeHealthRecords(Long nodeId, int page, int size);

    NodeMetricDTO getNodeMetric(Long clusterId, LocalDateTime from, LocalDateTime to);

    /** 注册集群：校验唯一、可选连通性检查、落库。 */
    ClusterDTO register(ClusterSpecDTO spec);

    /** 根据名称（租户内唯一）执行健康检查。 */
    ClusterHealthDTO healthByName(String name);
}
