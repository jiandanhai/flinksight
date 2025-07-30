package com.flinksight.backend.service;

import com.flinksight.backend.domain.MetricDashboard;
import com.flinksight.backend.domain.NodeHealth;
import com.flinksight.backend.mapper.MetricDashboardStructMapper;
import com.flinksight.backend.mapper.NodeHealthStructMapper;
import com.flinksight.backend.repository.NodeHealthRepository;
import com.flinksight.common.dto.MetricDashboardDTO;
import com.flinksight.common.dto.NodeHealthDTO;
import com.flinksight.common.service.NodeHealthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NodeHealthServiceImpl implements NodeHealthService {
    private final NodeHealthRepository repository;
    private final NodeHealthStructMapper mapper;

    @Override
    public NodeHealthDTO reportHealth(NodeHealthDTO nodeHealthDTO) {
        NodeHealth entity = mapper.toEntity(nodeHealthDTO);
        if (nodeHealthDTO.getId() == null) {
            entity.setCheckTime(LocalDateTime.now());
        }
        entity.setIsDeleted(0);
        NodeHealth saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<NodeHealthDTO> getLatestByNodeId(Long nodeId) {
        return repository.findTopByNodeIdAndIsDeletedOrderByCheckTimeDesc(nodeId,0).map(mapper::toDTO);
    }

    @Override
    public List<NodeHealthDTO> getByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId, 0));
    }

    @Override
    public List<NodeHealthDTO> getByNodeId(Long nodeId) {
        return mapper.toDTOList(repository.findByNodeIdAndIsDeleted(nodeId, 0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<NodeHealthDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            NodeHealthDTO  dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }

    @Override
    public boolean batchSoftDelete(List<Long> ids) {
        List<NodeHealthDTO> list = mapper.toDTOList(repository.findByIdInAndIsDeleted(ids, 0));
        List<NodeHealth> nhList = new ArrayList<>();
        for (NodeHealthDTO nh : list) {
            NodeHealth entity = mapper.toEntity(nh);
            entity.setIsDeleted(1);
            nhList.add(entity);
        }
        repository.saveAll(nhList);
        return true;
    }
}
