package com.flinksight.backend.service;

import com.flinksight.backend.domain.AlertHistory;
import com.flinksight.backend.domain.Cluster;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.AlertHistoryStructMapper;
import com.flinksight.backend.mapper.ClusterStructMapper;
import com.flinksight.backend.repository.ClusterRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.AlertHistoryDTO;
import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.service.ClusterService;
import com.flinksight.common.enums.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private final ClusterStructMapper mapper;


    @Override
    public ClusterDTO createOrUpdate(ClusterDTO clusterDTO) {
        Cluster entity = mapper.toEntity(clusterDTO);
        entity.setIsDeleted(0);
        Cluster saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<ClusterDTO> getClusterById(Long clusterId) {
        return repository.findById(clusterId).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<ClusterDTO> getClustersByTenant(Long tenantId) {
        return mapper.toDTOList(repository.findAllByTenantIdAndIsDeleted(tenantId,0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<ClusterDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            ClusterDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
