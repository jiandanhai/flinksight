package com.flinksight.common.service;

import com.flinksight.common.dto.HealthDistributionDTO;
import com.flinksight.common.dto.DashboardSummaryDTO;

public interface ClusterStatisticsService {

    /**
     * 获取集群健康分布
     */
    HealthDistributionDTO getHealthDistribution(Long tenantId);

    /**
     * 获取大盘核心数据
     */
    DashboardSummaryDTO getDashboardSummary(Long tenantId);
}
