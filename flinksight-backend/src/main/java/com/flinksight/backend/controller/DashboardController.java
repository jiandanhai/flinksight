package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.*;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 大盘统计聚合接口控制器
 * 无落库业务，无Repository
 */
@Slf4j
@RestController
@Tag(name = "api", description = "大盘统计聚合接口控制器")
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    private Long resolveTenantId(Long tenantId) {
        if (tenantId != null) return tenantId;
        // 如已在 SecurityContext 存了 tenantId，可从登录态解析；此处兜底返回 1
        return 1L;
    }


    @Operation(summary = "大盘核心指标", description = "大盘核心统计信息",
            operationId = "dashboardKpiStatisticsSummary")
    @GetMapping("/summary")
    public ApiResponse<KPIStatusSummaryDTO> getSummary(@RequestParam(required = false) Long tenantId) {
        log.info("#[dashboard summary] tenantId => {}", tenantId);
        return ApiResponse.ok(dashboardService.getDashboardSummary(resolveTenantId(tenantId)));
    }

    @Operation(summary = "集群健康分布", description = "集群健康状态分布信息",
            operationId = "dashboardStatisticsHealth")
    @GetMapping("/health")
    public ApiResponse<HealthDistributionDTO> getHealth(@RequestParam(required = false) Long tenantId) {
        log.info("#[dashboard health] tenantId => {}", tenantId);
        return ApiResponse.ok(dashboardService.getHealthDistribution(resolveTenantId(tenantId)));
    }

    @Operation(summary = "业务转化漏斗", description = "各阶段统计",
            operationId = "dashboardStatisticsJobFunnels")
    @GetMapping("/funnel")
    public ApiResponse<PageResult<JobFunnelDTO>> getFunnel(@RequestParam(required = false) Long tenantId,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "20") int size) {
        log.info("#[dashboard funnel] tenantId => {}", tenantId);
        return ApiResponse.ok(dashboardService.getJobFunnel(resolveTenantId(tenantId),page,size));
    }

    // ===== 新增：告警趋势 =====
    @Operation(summary = "告警趋势", description = "返回 times/total/fatal/warn",
            operationId = "dashboardStatisticsAlertTrend")
    @GetMapping("/alert-trend")
    public ApiResponse<AlertTrendDTO> getAlertTrend(
            @RequestParam(required = false) Long tenantId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        LocalDate end = (to == null) ? LocalDate.now() : to;
        LocalDate start = (from == null) ? end.minusDays(6) : from; // 默认近 7 天
        log.info("#[dashboard alert-trend] tenantId => {}", tenantId);
        return ApiResponse.ok(dashboardService.getAlertTrend(resolveTenantId(tenantId), start, end));
    }


    // ===== 新增：指标曲线（MetricDashboard）=====
    @Operation(summary = "指标曲线", operationId = "dashboardStatisticsMetricSeries")
    @GetMapping("/metrics/series")
    public ApiResponse<MetricSeriesDTO> getMetricSeries(
            @RequestParam(required = false) Long tenantId,
            @RequestParam String metric,
            @RequestParam String from,
            @RequestParam String  to
    ) {
        return ApiResponse.ok(dashboardService.getMetricSeries(resolveTenantId(tenantId), metric, LocalDateTime.parse(from),LocalDateTime.parse(to)));
    }

    @Operation(summary = "大屏监控指标卡", operationId = "dashboardStatisticsAlertCountByLevel")
    @GetMapping("/severity")
    public ResponseEntity<?> getAlertLevelStats(
            @RequestParam Long tenantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return ResponseEntity.ok(dashboardService.getAlertCountBySeverity(tenantId, start, end));
    }


    @Operation(summary = "", operationId = "dashboardStatisticsAlertCountByStatus")
    @GetMapping("/status")
    public ResponseEntity<?> getStatusStats(@RequestParam Long tenantId,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getAlertCountByStatus(tenantId, start, end));
    }

    @Operation(summary = "", operationId = "dashboardStatisticsResponseSeconds")
    @GetMapping("/response-time")
    public ResponseEntity<?> getResponseTime(@RequestParam Long tenantId) {
        return ResponseEntity.ok(Map.of("averageResponseSeconds", dashboardService.getAverageResponseSeconds(tenantId)));
    }

    @Operation(summary = "", operationId = "dashboardStatisticsFailedJobAlertTrend")
    @GetMapping("/job-fail-trend")
    public ResponseEntity<?> getFailedJobAlertTrend(@RequestParam Long tenantId,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(dashboardService.getFailedJobAlertTrend(tenantId, start, end));
    }

    @Operation(summary = "获取集群健康统计指标", operationId = "dashboardStatisticsClusterHealthMetrics")
    @GetMapping("/cluster-health")
    public ApiResponse<ClusterHealthMetricsDTO> getClusterHealthMetrics(@RequestParam @NotNull Long tenantId) {
        return ApiResponse.ok(dashboardService.getClusterHealthMetrics(tenantId));
    }

    @Operation(summary = "", operationId = "dashboardClusterTrend")
    @GetMapping("/cluster/trend")
    public ApiResponse<ClusterTrendDTO> getDashboardClusterTrend(
            @RequestParam Long tenantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime  to
    ) {
        return ApiResponse.ok(dashboardService.getClusterTrend(tenantId, from, to));
    }
}
