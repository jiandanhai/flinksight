package com.flinksight.common.service;

import com.flinksight.common.dto.JobFunnelDTO;
import com.flinksight.common.model.PageResult;


public interface JobStatisticsService {

    /**
     * 分页获取Job漏斗统计数据
     * @param tenantId 租户ID
     * @param page 页码（0起始）
     * @param size 每页大小
     * @return PageResult<JobFunnelDTO>
     */
    PageResult<JobFunnelDTO> getJobFunnel(Long tenantId, int page, int size);
}
