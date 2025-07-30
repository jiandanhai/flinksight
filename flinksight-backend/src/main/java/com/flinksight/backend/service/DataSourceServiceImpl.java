package com.flinksight.backend.service;

import com.flinksight.backend.domain.Cluster;
import com.flinksight.backend.domain.DataSource;
import com.flinksight.backend.mapper.ClusterStructMapper;
import com.flinksight.backend.mapper.DataSourceStructMapper;
import com.flinksight.backend.repository.DataSourceRepository;
import com.flinksight.common.dto.ApiAccessLogDTO;
import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.dto.DataSourceDTO;
import com.flinksight.common.service.DataSourceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DataSourceServiceImpl implements DataSourceService {
    private final DataSourceRepository repository;
    private final DataSourceStructMapper mapper;

    @Override
    public DataSourceDTO createOrUpdate(DataSourceDTO dataSourceDTO) {
        DataSource entity = mapper.toEntity(dataSourceDTO);
        entity.setIsDeleted(0);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public Optional<DataSourceDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<DataSourceDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId,0));
    }

    @Override
    public List<DataSourceDTO> getAll() {
        return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<DataSourceDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            DataSourceDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
