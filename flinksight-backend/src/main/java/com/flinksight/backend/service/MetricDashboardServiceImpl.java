package com.flinksight.backend.service;

import com.flinksight.backend.domain.MetricDashboard;
import com.flinksight.backend.mapper.MetricDashboardStructMapper;
import com.flinksight.backend.repository.MetricDashboardRepository;
import com.flinksight.common.dto.MetricDashboardDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.MetricDashboardService;
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
public class MetricDashboardServiceImpl implements MetricDashboardService {
    private final MetricDashboardRepository repository;
    private final MetricDashboardStructMapper metricDashboardStructMapper;

    @Override
    public MetricDashboardDTO createOrUpdate(MetricDashboardDTO metricDashboardDTO) {
        MetricDashboard entity = metricDashboardStructMapper.toEntity(metricDashboardDTO);
        entity.setIsDeleted(0);
        MetricDashboard saved = repository.save(entity);
        return metricDashboardStructMapper.toDTO(saved);
    }

    @Override
    public Optional<MetricDashboardDTO> getById(Long id) {
        return repository.findById(id).map(metricDashboardStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }


    @Override
    public PageResult<MetricDashboardDTO> findByTenantId(Long tenantId,int page, int size) {
        Page<MetricDashboard> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<MetricDashboardDTO> dtoPage = result.map(metricDashboardStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<MetricDashboardDTO> getAll(int page, int size) {
        Page<MetricDashboard> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<MetricDashboardDTO> dtoPage = result.map(metricDashboardStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<MetricDashboardDTO> opt = repository.findById(id).map(metricDashboardStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            MetricDashboardDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(metricDashboardStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
