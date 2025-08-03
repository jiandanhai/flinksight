package com.flinksight.common.service;

import com.flinksight.common.dto.NodeHealthDTO;
import com.flinksight.common.model.PageResult;

import java.util.List;
import java.util.Optional;

public interface NodeHealthService extends SoftDeleteService<NodeHealthDTO, Long> {
    NodeHealthDTO reportHealth(NodeHealthDTO health);

    Optional<NodeHealthDTO> getLatestByNodeId(Long nodeId);

    PageResult<NodeHealthDTO> getByTenantId(Long tenantId,int page, int size);

    PageResult<NodeHealthDTO> getByNodeId(Long nodeId,int page, int size);

    boolean batchSoftDelete(List<Long> ids);
}
