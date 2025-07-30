package com.flinksight.backend.service;

import com.flinksight.backend.domain.JobInstance;
import com.flinksight.backend.mapper.JobDiagnosticLogStructMapper;
import com.flinksight.backend.mapper.JobInstanceStructMapper;
import com.flinksight.backend.repository.JobInstanceRepository;
import com.flinksight.common.dto.FileDTO;
import com.flinksight.common.dto.JobInstanceDTO;
import com.flinksight.common.service.JobInstanceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class JobInstanceServiceImpl implements JobInstanceService {
    private final JobInstanceRepository repository;
    private final JobInstanceStructMapper mapper;

    @Override
    public JobInstanceDTO createJob(JobInstanceDTO jobInstanceDTO) {
        JobInstance entity = mapper.toEntity(jobInstanceDTO);
        entity.setIsDeleted(0);
        entity.setStatus("INIT");
        JobInstance saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public boolean updateJobStatus(Long id, String status) {
        Optional<JobInstanceDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            JobInstanceDTO jobInstanceDTO = opt.get();
            JobInstance entity = mapper.toEntity(jobInstanceDTO);
            entity.setStatus(status);
            repository.save(entity);
            return true;
        }
        return false;
    }

    @Override
    public List<JobInstanceDTO> listByTenant(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId,0));
    }

    @Override
    public List<JobInstanceDTO> listByStatus(String status) {
        return mapper.toDTOList(repository.findByStatusAndIsDeleted(status,0));
    }

    @Override
    public Optional<JobInstanceDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<JobInstanceDTO> findByTenantIdAndIsDeleted(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId,0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<JobInstanceDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            JobInstanceDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
