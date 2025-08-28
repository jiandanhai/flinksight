package com.flinksight.backend.service;

import com.flinksight.backend.domain.Node;
import com.flinksight.backend.domain.NodeHealth;
import com.flinksight.backend.mapper.NodeStructMapper;
import com.flinksight.backend.repository.NodeHealthRepository;
import com.flinksight.backend.repository.NodeRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.dto.NodeHealthPointDTO;
import com.flinksight.common.dto.NodeHealthResponseDTO;
import com.flinksight.common.enums.NodeState;
import com.flinksight.common.service.NodeService;
import com.flinksight.common.service.projection.NodeListRow;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        entity.setStatus(NodeState.ENABLED);   // 例如新增节点默认启用
        Node saved = repository.save(entity);
        return nodeStructMapper.toDTO(saved);
    }

    @Override
    public Optional<NodeDTO> getById(Long id) {
        return repository.findById(id).map(nodeStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public NodeHealthResponseDTO getNodeHealthSeries(Long nodeId, LocalDateTime from, LocalDateTime to) {
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

    /** 节点列表（带最新健康）—— 前端两个 Tab 都调它 */
    public Page<NodeListRow> pageNodesWithHealth(Long clusterId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "id"));
        return repository.pageWithLatestHealth(SecurityUtil.getCurrentTenantId(), clusterId, keyword, pageable);
    }

    /** 环图统计（与列表完全同口径） */
    public Map<String, Long> healthBuckets(Long clusterId) {
        Map<String, Long> map = new HashMap<>();
        repository.healthBuckets(SecurityUtil.getCurrentTenantId(), clusterId).forEach(arr -> {
            String health = (String) arr[0];
            Number cnt = (Number) arr[1];
            map.put(health, cnt.longValue());
        });
        // 填补缺失桶
        map.putIfAbsent("HEALTHY", 0L);
        map.putIfAbsent("WARNING", 0L);
        map.putIfAbsent("UNHEALTHY", 0L);
        map.putIfAbsent("UNKNOWN", 0L);
        return map;
    }

    /** 单节点健康历史（节点详情/弹窗用；列表不要 N+1） */
    public Map<String, Object> nodeHealthHistory(Long nodeId, LocalDateTime from, LocalDateTime to, int page, int size) {
        var pageable = PageRequest.of(Math.max(0, page - 1), size, Sort.by(Sort.Direction.DESC, "checkTime"));
        var pageData = nodeHealthRepository.pageHistory(SecurityUtil.getCurrentTenantId(), nodeId, from, to, pageable);
        var latest = nodeHealthRepository.findTopByTenantIdAndNodeIdOrderByCheckTimeDesc(SecurityUtil.getCurrentTenantId(), nodeId).orElse(null);
        Map<String, Object> res = new HashMap<>();
        res.put("latestStatus", latest != null ? latest.getHealthStatus() : "UNKNOWN");
        res.put("items", pageData.getContent());
        res.put("total", pageData.getTotalElements());
        return res;
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
