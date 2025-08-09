package com.flinksight.common.service;

import com.flinksight.common.dto.DashboardSummaryDTO;
import com.flinksight.common.dto.HealthDistributionDTO;
import com.flinksight.common.dto.JobFunnelDTO;
import com.flinksight.common.model.PageResult;

/**
 * 统计聚合业务接口
 * 仅做聚合查询，无持久化，无Repository
 */
public interface DashboardService {

    /**
     * 查询大盘核心指标
     * @param tenantId 租户ID
     * @return 大盘统计DTO
     */
    DashboardSummaryDTO getDashboardSummary(Long tenantId);

    /**
     * 查询集群健康分布
     * @param tenantId 租户ID
     * @return 健康分布DTO
     */
    HealthDistributionDTO getHealthDistribution(Long tenantId);

    /**
     * 查询业务转化漏斗数据
     * @param tenantId 租户ID
     * @return 漏斗统计DTO列表
     */
    PageResult<JobFunnelDTO> getJobFunnel(Long tenantId, int page, int size);
}
