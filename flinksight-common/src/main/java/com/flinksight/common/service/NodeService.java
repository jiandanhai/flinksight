package com.flinksight.common.service;

import com.flinksight.common.dto.ApiKeyDTO;
import com.flinksight.common.dto.LabelDTO;
import com.flinksight.common.dto.NodeDTO;

import java.util.List;
import java.util.Optional;

public interface NodeService extends SoftDeleteService<LabelDTO, Long> {
    NodeDTO createOrUpdate(NodeDTO node);
    Optional<NodeDTO> getById(Long id);
    List<NodeDTO> findByClusterId(Long clusterId);
    List<NodeDTO> getAll();
}
