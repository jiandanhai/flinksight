package com.flinksight.backend.service;

import com.flinksight.backend.domain.IntegrationConfig;
import com.flinksight.backend.domain.JobInstance;
import com.flinksight.backend.mapper.IntegrationConfigStructMapper;
import com.flinksight.backend.mapper.JobInstanceStructMapper;
import com.flinksight.backend.repository.JobInstanceRepository;
import com.flinksight.common.dto.JobInstanceDTO;
import com.flinksight.common.service.JobAutoRecoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobAutoRecoverServiceImpl implements JobAutoRecoverService {

    private final JobInstanceRepository repo;
    private final JobInstanceStructMapper mapper;
    // 需集成实际调度API

    @Override
    public void checkAndRestart(JobInstanceDTO jobInstanceDTO) {
        if ("FAILED".equals(jobInstanceDTO.getStatus())) {

            JobInstance entity = mapper.toEntity(jobInstanceDTO);
            entity.setStatus("RESTARTING");
            // 调用Flink/Spark API重启任务
            // FlinkApiHelper.restartJob(job.getClusterId(), job.getId());
            repo.save(entity);
        }
    }
}
