package com.flinksight.backend.service;

import com.flinksight.backend.domain.LoginHistory;
import com.flinksight.backend.domain.MetricDashboard;
import com.flinksight.backend.mapper.LoginHistoryStructMapper;
import com.flinksight.backend.mapper.MetricDashboardStructMapper;
import com.flinksight.backend.repository.MetricDashboardRepository;
import com.flinksight.common.dto.LoginHistoryDTO;
import com.flinksight.common.dto.MetricDashboardDTO;
import com.flinksight.common.service.MetricDashboardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MetricDashboardServiceImpl implements MetricDashboardService {
    private final MetricDashboardRepository repository;
    private final MetricDashboardStructMapper mapper;

    @Override
    public MetricDashboardDTO createOrUpdate(MetricDashboardDTO metricDashboardDTO) {
        MetricDashboard entity = mapper.toEntity(metricDashboardDTO);
        entity.setIsDeleted(0);
        MetricDashboard saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<MetricDashboardDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }


    @Override
    public List<MetricDashboardDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId, 0));
    }

    @Override
    public List<MetricDashboardDTO> getAll() {
        return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<MetricDashboardDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            MetricDashboardDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
