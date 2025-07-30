package com.flinksight.backend.service;

import com.flinksight.backend.domain.IntegrationConfig;
import com.flinksight.backend.domain.JobAlertRule;
import com.flinksight.backend.domain.JobAlertLog;
import com.flinksight.backend.mapper.IntegrationConfigStructMapper;
import com.flinksight.backend.mapper.JobAlertLogStructMapper;
import com.flinksight.backend.mapper.JobAlertRuleStructMapper;
import com.flinksight.backend.repository.JobAlertRuleRepository;
import com.flinksight.backend.repository.JobAlertLogRepository;
import com.flinksight.common.dto.JobAlertRuleDTO;
import com.flinksight.common.service.JobAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobAlertServiceImpl implements JobAlertService {

    private final JobAlertRuleRepository ruleRepo;
    private final JobAlertLogRepository logRepo;
    private final JobAlertRuleStructMapper ruleMapper;
    private final JobAlertLogStructMapper logMapper;

    @Override
    public void checkAndAlert(JobAlertRuleDTO jobAlertRuleDTO, Long jobId, String jobName, String metricValue) {
        // 真实业务应有表达式计算，可用js引擎等判断，以下为伪代码
        if ("EXCEPTION".equals(jobAlertRuleDTO.getAlertType()) && "EXCEPTION".equals(metricValue)) {
            JobAlertLog log = JobAlertLog.builder()
                    .tenantId(jobAlertRuleDTO.getTenantId())
                    .jobId(jobId)
                    .jobName(jobName)
                    .alertType(jobAlertRuleDTO.getAlertType())
                    .alertMsg("检测到作业异常：" + metricValue)
                    .alertTime(LocalDateTime.now())
                    .status("SENT")
                    .isDeleted(0)
                    .build();
            logRepo.save(log);
            // 推送通知（可集成钉钉、短信、邮件、Webhook等）
        }
    }

    @Override
    public List<JobAlertRuleDTO> getActiveRulesByTenant(Long tenantId) {
        return ruleMapper.toDTOList(ruleRepo.findByTenantIdAndIsDeleted(tenantId,0));
    }

    @Override
    public void acknowledgeAlert(Long alertLogId) {
        logRepo.findById(alertLogId).map(logMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        logRepo.findById(alertLogId).map(logMapper::toDTO).filter(e -> e.getIsDeleted() == 0).ifPresent(alertLogDTO -> {
            JobAlertLog entity = logMapper.toEntity(alertLogDTO);
            entity.setStatus("ACK");
            logRepo.save(entity);
        });
    }
}
