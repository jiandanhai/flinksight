package com.flinksight.common.service;

import com.flinksight.common.dto.JobAlertRuleDTO;

import java.util.List;

public interface JobAlertService {
    void checkAndAlert(JobAlertRuleDTO rule, Long jobId, String jobName, String metricValue);

    List<JobAlertRuleDTO> getActiveRulesByTenant(Long tenantId);

    void acknowledgeAlert(Long alertLogId);
}
