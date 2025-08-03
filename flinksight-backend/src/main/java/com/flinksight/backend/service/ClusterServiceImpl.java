package com.flinksight.backend.service;

import com.flinksight.backend.domain.Cluster;
import com.flinksight.backend.mapper.ClusterStructMapper;
import com.flinksight.backend.repository.ClusterRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ClusterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 集群业务实现
 * Cluster Service Impl
 */
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class ClusterServiceImpl implements ClusterService{

    private final ClusterRepository repository;
    private final ClusterStructMapper clusterStructMapper;


    @Override
    public ClusterDTO createOrUpdate(ClusterDTO clusterDTO) {
        Cluster entity = clusterStructMapper.toEntity(clusterDTO);
        entity.setIsDeleted(0);
        Cluster saved = repository.save(entity);
        return clusterStructMapper.toDTO(saved);
    }

    @Override
    public Optional<ClusterDTO> getClusterById(Long clusterId) {
        return repository.findById(clusterId).map(clusterStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<ClusterDTO> getClustersByTenant(Long tenantId,int page, int size) {
        Page<Cluster> result = repository.findAllByTenantIdAndIsDeleted(tenantId, 0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<ClusterDTO> dtoPage = result.map(clusterStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<ClusterDTO> opt = repository.findById(id).map(clusterStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ClusterDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(clusterStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
