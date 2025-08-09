package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.DashboardSummaryDTO;
import com.flinksight.common.dto.HealthDistributionDTO;
import com.flinksight.common.dto.JobFunnelDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 大盘统计聚合接口控制器
 * 无落库业务，无Repository
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 获取大盘核心指标
     */
    @Operation(
            summary = "大盘核心指标",
            description = "大盘核心统计信息",
            operationId = "getDashboardStatisticsSummaryByTenant"
    )
    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryDTO> getSummary(@RequestParam Long tenantId) {
        return ApiResponse.ok(dashboardService.getDashboardSummary(tenantId));
    }

    /**
     * 获取集群健康分布
     */
    @Operation(
            summary = "集群健康分布",
            description = "集群健康状态分布信息",
            operationId = "getDashboardStatisticsHealthByTenant"
    )
    @GetMapping("/health")
    public ApiResponse<HealthDistributionDTO> getHealth(@RequestParam Long tenantId) {
        return ApiResponse.ok(dashboardService.getHealthDistribution(tenantId));
    }

    /**
     * 获取业务转化漏斗数据
     */
    @Operation(
            summary = "集群健康分布",
            description = "集群健康状态分布信息",
            operationId = "getJobFunnelsByTenant"
    )
    @GetMapping("/funnel")
    public ApiResponse<PageResult<JobFunnelDTO>> getFunnel(@RequestParam Long tenantId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(dashboardService.getJobFunnel(tenantId,page,size));
    }
}
