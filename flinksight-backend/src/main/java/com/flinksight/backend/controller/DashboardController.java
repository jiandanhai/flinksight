package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.*;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "大盘核心指标", description = "大盘核心统计信息",
            operationId = "dashboardKpiStatisticsSummary")
    @GetMapping("/summary")
    public ApiResponse<KPIStatusSummaryDTO> getSummary() {
        return ApiResponse.ok(dashboardService.getDashboardSummary());
    }

    @Operation(summary = "集群健康分布", description = "集群健康状态分布信息",
            operationId = "dashboardStatisticsHealth")
    @GetMapping("/health")
    public ApiResponse<HealthDistributionDTO> getHealth(@RequestParam(required = false) Long tenantId) {
        log.info("#[dashboard health] tenantId => {}", tenantId);
        return ApiResponse.ok(dashboardService.getHealthDistribution());
    }

    @Operation(summary = "业务转化漏斗", description = "各阶段统计",
            operationId = "dashboardStatisticsJobFunnels")
    @GetMapping("/funnel")
    public ApiResponse<PageResult<JobFunnelDTO>> getFunnel(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(dashboardService.getJobFunnel(page,size));
    }

    // ===== 新增：告警趋势 =====
    @Operation(summary = "告警趋势", description = "返回 times/total/fatal/warn",
            operationId = "dashboardStatisticsAlertTrend")
    @GetMapping("/alert-trend")
    public ApiResponse<AlertTrendDTO> getAlertTrend(@RequestParam(required = false)
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
                                                    @RequestParam(required = false)
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant  to) {

        ZoneId zone = ZoneId.systemDefault(); // 或固定 ZoneId.of("Asia/Shanghai") / "UTC"
        LocalDate end = (to == null)
                ? LocalDate.now(zone)
                : LocalDateTime.ofInstant(to, zone).toLocalDate();

        LocalDate start = (from == null)
                ? end.minusDays(6)
                : LocalDateTime.ofInstant(from, zone).toLocalDate();
        return ApiResponse.ok(dashboardService.getAlertTrend(start, end));
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
        return ApiResponse.ok(dashboardService.getMetricSeries(metric, LocalDateTime.parse(from),LocalDateTime.parse(to)));
    }

    @Operation(summary = "大屏监控指标卡", operationId = "dashboardStatisticsAlertCountByLevel")
    @GetMapping("/severity")
    public ApiResponse<?> getAlertLevelStats(@RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]X")
            LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]X")
            LocalDateTime to)
     {
         LocalDateTime end = (to == null) ? LocalDateTime.now() : to;
         LocalDateTime start = (from == null) ? end.minusDays(6) : from;
        return ApiResponse.ok(dashboardService.getAlertCountBySeverity(start, end));
    }


    @Operation(summary = "", operationId = "dashboardStatisticsAlertCountByStatus")
    @GetMapping("/status")
    public ApiResponse<?> getStatusStats(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ApiResponse.ok(dashboardService.getAlertCountByStatus(start, end));
    }

    @Operation(summary = "", operationId = "dashboardStatisticsResponseSeconds")
    @GetMapping("/response-time")
    public ApiResponse<?> getResponseTime() {
        return ApiResponse.ok(Map.of("averageResponseSeconds", dashboardService.getAverageResponseSeconds()));
    }

    @Operation(summary = "", operationId = "dashboardStatisticsFailedJobAlertTrend")
    @GetMapping("/job-fail-trend")
    public ApiResponse<?> getFailedJobAlertTrend(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ApiResponse.ok(dashboardService.getFailedJobAlertTrend(start, end));
    }

    @Operation(summary = "获取集群健康统计指标", operationId = "dashboardStatisticsClusterHealthMetrics")
    @GetMapping("/cluster-health")
    public ApiResponse<ClusterHealthMetricsDTO> getClusterHealthMetrics() {
        return ApiResponse.ok(dashboardService.getClusterHealthMetrics());
    }

    @Operation(summary = "", operationId = "dashboardClusterTrend")
    @GetMapping("/cluster/trend")
    public ApiResponse<ClusterTrendDTO> getDashboardClusterTrend(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime  to
    ) {
        return ApiResponse.ok(dashboardService.getClusterTrend(from, to));
    }
}
