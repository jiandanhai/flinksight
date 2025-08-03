package com.flinksight.common.service;

import com.flinksight.common.dto.JobAlertRuleDTO;
import com.flinksight.common.model.PageResult;

public interface JobAlertService {
    void checkAndAlert(JobAlertRuleDTO rule, Long jobId, String jobName, String metricValue);

    PageResult<JobAlertRuleDTO> getActiveRulesByTenant(Long tenantId,int page, int size);

    void acknowledgeAlert(Long alertLogId);
}
