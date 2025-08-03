package com.flinksight.common.service;

import com.flinksight.common.dto.LabelDTO;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

public interface NodeService extends SoftDeleteService<LabelDTO, Long> {
    NodeDTO createOrUpdate(NodeDTO node);
    Optional<NodeDTO> getById(Long id);
    PageResult<NodeDTO> findByClusterId(Long clusterId,int page, int size);
    PageResult<NodeDTO> getAll(int page, int size);
}
