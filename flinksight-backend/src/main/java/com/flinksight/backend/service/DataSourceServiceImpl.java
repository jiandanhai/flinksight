package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.DataSource;
import com.flinksight.backend.mapper.DataSourceStructMapper;
import com.flinksight.backend.repository.DataSourceRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.DataSourceDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DataSourceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DataSourceServiceImpl implements DataSourceService {
    private final DataSourceRepository repository;
    private final DataSourceStructMapper dataSourceStructMapper;

    @Override
    public DataSourceDTO createOrUpdate(DataSourceDTO dataSourceDTO) {
        DataSource entity = dataSourceStructMapper.toEntity(dataSourceDTO);
        entity.setIsDeleted(0);
        return dataSourceStructMapper.toDTO(repository.save(entity));
    }

    @Override
    public Optional<DataSourceDTO> getById(Long id) {
        return repository.findById(id).map(dataSourceStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<DataSourceDTO> list(int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, DataSource.class); // 统一 1→0
        Page<DataSource> result = repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(), 0, pr);
        return PageHelpers.toPageResult(result, dataSourceStructMapper::toDTO, true); // 返回 1-ba
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<DataSourceDTO> opt = repository.findById(id).map(dataSourceStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            DataSourceDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(dataSourceStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
