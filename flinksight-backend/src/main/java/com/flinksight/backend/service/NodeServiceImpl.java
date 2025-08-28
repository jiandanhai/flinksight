package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.Node;
import com.flinksight.backend.domain.NodeHealth;
import com.flinksight.backend.mapper.NodeStructMapper;
import com.flinksight.backend.repository.NodeHealthRepository;
import com.flinksight.backend.repository.NodeRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.dto.NodeHealthPointDTO;
import com.flinksight.common.dto.NodeHealthResponseDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NodeServiceImpl implements NodeService {
    private final NodeRepository repository;
    private final NodeStructMapper nodeStructMapper;
    private final NodeHealthRepository nodeHealthRepository;

    @Override
    public NodeDTO createOrUpdate(NodeDTO nodeDTO) {
        Node entity = nodeStructMapper.toEntity(nodeDTO);
        entity.setIsDeleted(0);
        Node saved = repository.save(entity);
        return nodeStructMapper.toDTO(saved);
    }

    @Override
    public Optional<NodeDTO> getById(Long id) {
        return repository.findById(id).map(nodeStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<NodeDTO> list(Long clusterId,int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, Node.class); // 统一 1→0
        Page<Node> result = repository.findByClusterIdAndIsDeleted(clusterId,0, pr);
        return PageHelpers.toPageResult(result, nodeStructMapper::toDTO, true); // 返回
    }

    @Override
    public NodeHealthResponseDTO getNodeHealth(Long nodeId, LocalDateTime from, LocalDateTime to) {
        List<NodeHealth> list = nodeHealthRepository
                .findAllByTenantIdAndNodeIdAndCheckTimeBetweenOrderByCheckTime(SecurityUtil.getCurrentTenantId(), nodeId, from, to);

        String latest = nodeHealthRepository
                .findTopByTenantIdAndNodeIdOrderByCheckTimeDesc(SecurityUtil.getCurrentTenantId(), nodeId)
                .map(NodeHealth::getHealthStatus).orElse("UNKNOWN");

        return NodeHealthResponseDTO.builder()
                .latestStatus(latest)
                .items(list.stream()
                        .map(n -> new NodeHealthPointDTO(n.getCheckTime(), n.getHealthStatus(), n.getMessage()))
                        .toList())
                .build();
    }


    @Override
    public boolean sDelete(Long id) {
        Optional<NodeDTO> opt = repository.findById(id).map(nodeStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            NodeDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(nodeStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
