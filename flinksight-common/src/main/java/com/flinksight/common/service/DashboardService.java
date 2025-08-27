package com.flinksight.common.service;

import com.flinksight.common.dto.*;
import com.flinksight.common.model.PageResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 统计聚合业务接口
 * 仅做聚合查询，无持久化，无Repository
 */
public interface DashboardService {

    /**
     * 查询大盘核心指标
     * @return 大盘统计DTO
     */
    KPIStatusSummaryDTO getDashboardSummary();

    /**
     * 查询集群健康分布
     * @return 健康分布DTO
     */
    HealthDistributionDTO getHealthDistribution();


    AlertTrendDTO getAlertTrend(LocalDate from, LocalDate to);

    /**
     * 查询业务转化漏斗数据
     * @return 漏斗统计DTO列表
     */
    PageResult<JobFunnelDTO> getJobFunnel(int page, int size);

    // 监控卡片四格
    MonitorMetricsDTO getMonitorMetrics();

    // 指标曲线
    MetricSeriesDTO getMetricSeries(String metric, LocalDateTime from, LocalDateTime to);

    List<Map<String, Object>> getAlertCountBySeverity(LocalDateTime start, LocalDateTime end);


    List<Map<String, Object>> getAlertCountByStatus(LocalDateTime start, LocalDateTime end);

    Double getAverageResponseSeconds();

    List<Map<String, Object>> getFailedJobAlertTrend(LocalDateTime start, LocalDateTime end);


    /**
     * 获取集群健康统计信息
     * @return 集群健康指标
     */
    ClusterHealthMetricsDTO getClusterHealthMetrics();


    ClusterTrendDTO getClusterTrend(LocalDateTime from, LocalDateTime to);
}
