package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.NodeHealth;
import com.flinksight.backend.mapper.NodeHealthStructMapper;
import com.flinksight.backend.repository.NodeHealthRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.NodeHealthDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NodeHealthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final NodeHealthStructMapper nodeHealthStructMapper;

    @Override
    public NodeHealthDTO reportHealth(NodeHealthDTO nodeHealthDTO) {
        NodeHealth entity = nodeHealthStructMapper.toEntity(nodeHealthDTO);
        if (nodeHealthDTO.getId() == null) {
            entity.setCheckTime(LocalDateTime.now());
        }
        entity.setIsDeleted(0);
        NodeHealth saved = repository.save(entity);
        return nodeHealthStructMapper.toDTO(saved);
    }

    @Override
    public Optional<NodeHealthDTO> getLatestByNodeId(Long nodeId) {
        return repository.findTopByTenantIdAndNodeIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),nodeId,0).map(nodeHealthStructMapper::toDTO);
    }

    @Override
    public PageResult<NodeHealthDTO> list(Long nodeId,int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, NodeHealth.class); // 统一 1→0
        Page<NodeHealth> result = (nodeId == null)
                ? repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentUserId(), 0, pr)
                : repository.findByTenantIdAndNodeIdAndIsDeleted(SecurityUtil.getCurrentUserId(), nodeId,0, pr);
        return PageHelpers.toPageResult(result, nodeHealthStructMapper::toDTO, true); // 返回
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<NodeHealthDTO> opt = repository.findById(id).map(nodeHealthStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            NodeHealthDTO  dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(nodeHealthStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }

    @Override
    public boolean batchSoftDelete(List<Long> ids) {
        List<NodeHealthDTO> list = nodeHealthStructMapper.toDTOList(repository.findByTenantIdAndIdInAndIsDeleted(SecurityUtil.getCurrentTenantId(),ids, 0));
        List<NodeHealth> nhList = new ArrayList<>();
        for (NodeHealthDTO nh : list) {
            NodeHealth entity = nodeHealthStructMapper.toEntity(nh);
            entity.setIsDeleted(1);
            nhList.add(entity);
        }
        repository.saveAll(nhList);
        return true;
    }
}
