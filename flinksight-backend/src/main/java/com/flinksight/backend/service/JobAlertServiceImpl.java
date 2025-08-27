package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.JobAlertLog;
import com.flinksight.backend.domain.JobAlertRule;
import com.flinksight.backend.mapper.JobAlertLogStructMapper;
import com.flinksight.backend.mapper.JobAlertRuleStructMapper;
import com.flinksight.backend.repository.JobAlertLogRepository;
import com.flinksight.backend.repository.JobAlertRuleRepository;
import com.flinksight.common.dto.JobAlertRuleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JobAlertServiceImpl implements JobAlertService {

    private final JobAlertRuleRepository ruleRepo;
    private final JobAlertLogRepository logRepo;
    private final JobAlertRuleStructMapper jobAlertRuleStructMapper;
    private final JobAlertLogStructMapper jobAlertLogStructMapper;

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
    public PageResult<JobAlertRuleDTO> list(Long tenantId,int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, JobAlertRule.class); // 统一 1→0
        Page<JobAlertRule> result = ruleRepo.findByTenantIdAndIsDeleted(tenantId,0, pr);
        return PageHelpers.toPageResult(result, jobAlertRuleStructMapper::toDTO, true); // 返回 1-b
    }

    @Override
    public void acknowledgeAlert(Long alertLogId) {
        logRepo.findById(alertLogId).map(jobAlertLogStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        logRepo.findById(alertLogId).map(jobAlertLogStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0).ifPresent(alertLogDTO -> {
            JobAlertLog entity = jobAlertLogStructMapper.toEntity(alertLogDTO);
            entity.setStatus("ACK");
            logRepo.save(entity);
        });
    }
}
