package com.flinksight.common.service;

import com.flinksight.common.dto.NodeHealthDTO;

import java.util.List;
import java.util.Optional;

public interface NodeHealthService extends SoftDeleteService<NodeHealthDTO, Long> {
    NodeHealthDTO reportHealth(NodeHealthDTO health);

    Optional<NodeHealthDTO> getLatestByNodeId(Long nodeId);

    List<NodeHealthDTO> getByTenantId(Long tenantId);

    List<NodeHealthDTO> getByNodeId(Long nodeId);

    boolean batchSoftDelete(List<Long> ids);
}
