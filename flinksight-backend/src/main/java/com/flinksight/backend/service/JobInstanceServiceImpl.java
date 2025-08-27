package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.JobInstance;
import com.flinksight.backend.mapper.JobInstanceStructMapper;
import com.flinksight.backend.repository.JobInstanceRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.JobInstanceDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobInstanceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class JobInstanceServiceImpl implements JobInstanceService {
    private final JobInstanceRepository repository;
    private final JobInstanceStructMapper jobInstanceStructMapper;

    @Override
    public JobInstanceDTO createJob(JobInstanceDTO jobInstanceDTO) {
        JobInstance entity = jobInstanceStructMapper.toEntity(jobInstanceDTO);
        entity.setIsDeleted(0);
        entity.setStatus(0);
        JobInstance saved = repository.save(entity);
        return jobInstanceStructMapper.toDTO(saved);
    }

    @Override
    public boolean updateJobStatus(Long id, Integer status) {
        Optional<JobInstanceDTO> opt = repository.findById(id).map(jobInstanceStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            JobInstanceDTO jobInstanceDTO = opt.get();
            JobInstance entity = jobInstanceStructMapper.toEntity(jobInstanceDTO);
            entity.setStatus(status);
            repository.save(entity);
            return true;
        }
        return false;
    }

    @Override
    public PageResult<JobInstanceDTO> list(Integer status,int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, JobInstance.class); // 统一 1→0
        Page<JobInstance> result;
        if(status==-1){
            result = repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),0,pr);
        } else {
            result = repository.findByTenantIdAndStatusAndIsDeleted(SecurityUtil.getCurrentTenantId(),status,0,pr);
        }
        return PageHelpers.toPageResult(result, jobInstanceStructMapper::toDTO, true); // 返回 1-b
    }

    @Override
    public Optional<JobInstanceDTO> getById(Long id) {
        return repository.findById(id).map(jobInstanceStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public Map<Integer, Long> countStatusByTenantId() {
        List<Object[]> results = repository.countStatusByTenantId(SecurityUtil.getCurrentTenantId());
        Map<Integer, Long> map = new HashMap<>();
        for (Object[] row : results) {
            map.put((Integer) row[0], (Long) row[1]);
        }
        return map;
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<JobInstanceDTO> opt = repository.findById(id).map(jobInstanceStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            JobInstanceDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(jobInstanceStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
