package com.flinksight.backend.service;

import com.flinksight.backend.domain.JobInstance;
import com.flinksight.backend.mapper.JobInstanceStructMapper;
import com.flinksight.backend.repository.JobInstanceRepository;
import com.flinksight.common.dto.JobInstanceDTO;
import com.flinksight.common.enums.JobStatus;
import com.flinksight.common.service.JobAutoRecoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobAutoRecoverServiceImpl implements JobAutoRecoverService {

    private final JobInstanceRepository repo;
    private final JobInstanceStructMapper jobInstanceStructMapper;
    // 需集成实际调度API

    @Override
    public void checkAndRestart(JobInstanceDTO jobInstanceDTO) {
        if (JobStatus.FAILED.getCode() == (jobInstanceDTO.getStatus())) {

            JobInstance entity = jobInstanceStructMapper.toEntity(jobInstanceDTO);
            entity.setStatus(JobStatus.RESTARTING.getCode());
            repo.save(entity);
            // 调用Flink/Spark API重启任务
            // FlinkApiHelper.restartJob(job.getClusterId(), job.getId());
            repo.save(entity);
        }
    }
}
