package com.flinksight.backend.service;

import com.flinksight.backend.domain.Job;
import com.flinksight.backend.domain.Node;
import com.flinksight.backend.mapper.JobStructMapper;
import com.flinksight.backend.mapper.NodeStructMapper;
import com.flinksight.backend.repository.NodeRepository;
import com.flinksight.common.dto.JobDTO;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.service.NodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NodeServiceImpl implements NodeService {
    private final NodeRepository repository;
    private final NodeStructMapper mapper;

    @Override
    public NodeDTO createOrUpdate(NodeDTO nodeDTO) {
        Node entity = mapper.toEntity(nodeDTO);
        entity.setIsDeleted(0);
        Node saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<NodeDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<NodeDTO> findByClusterId(Long clusterId) {
        return mapper.toDTOList(repository.findByClusterIdAndIsDeleted(clusterId, 0));
    }

    @Override
    public List<NodeDTO> getAll() {
        return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }


    @Override
    public boolean softDelete(Long id) {
        Optional<NodeDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            NodeDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
