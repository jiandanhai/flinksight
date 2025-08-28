package com.flinksight.common.service;

import com.flinksight.common.dto.LabelDTO;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.dto.NodeHealthResponseDTO;
import com.flinksight.common.service.projection.NodeListRow;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public interface NodeService extends SoftDeleteService<LabelDTO, Long> {
    NodeDTO createOrUpdate(NodeDTO node);
    Optional<NodeDTO> getById(Long id);
    NodeHealthResponseDTO getNodeHealthSeries(Long nodeId, LocalDateTime from, LocalDateTime to);

    Page<NodeListRow> pageNodesWithHealth(Long clusterId, String keyword, int page, int size);

    Map<String, Long> healthBuckets(Long clusterId);

    Map<String, Object> nodeHealthHistory(Long nodeId, LocalDateTime from, LocalDateTime to, int page, int size);
}
