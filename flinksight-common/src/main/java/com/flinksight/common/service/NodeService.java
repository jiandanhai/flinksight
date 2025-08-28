package com.flinksight.common.service;

import com.flinksight.common.dto.LabelDTO;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.dto.NodeHealthResponseDTO;
import com.flinksight.common.model.PageResult;

import java.time.LocalDateTime;
import java.util.Optional;

public interface NodeService extends SoftDeleteService<LabelDTO, Long> {
    NodeDTO createOrUpdate(NodeDTO node);
    Optional<NodeDTO> getById(Long id);
    PageResult<NodeDTO> list(Long clusterId,int page, int size);
    NodeHealthResponseDTO getNodeHealth(Long nodeId, LocalDateTime from, LocalDateTime to);
}
