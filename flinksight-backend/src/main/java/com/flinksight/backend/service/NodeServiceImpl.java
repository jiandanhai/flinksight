package com.flinksight.backend.service;

import com.flinksight.backend.domain.Node;
import com.flinksight.backend.mapper.NodeStructMapper;
import com.flinksight.backend.repository.NodeRepository;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NodeServiceImpl implements NodeService {
    private final NodeRepository repository;
    private final NodeStructMapper nodeStructMapper;

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
    public PageResult<NodeDTO> findByClusterId(Long clusterId,int page, int size) {
        Page<Node> result = repository.findByClusterIdAndIsDeleted(clusterId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<NodeDTO> dtoPage = result.map(nodeStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<NodeDTO> getAll(int page, int size) {
        Page<Node> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<NodeDTO> dtoPage = result.map(nodeStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }


    @Override
    public boolean softDelete(Long id) {
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
