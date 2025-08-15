package com.flinksight.backend.service;

import com.flinksight.backend.domain.Alert;
import com.flinksight.backend.domain.Cluster;
import com.flinksight.backend.domain.ClusterStatusHistory;
import com.flinksight.backend.repository.*;
import com.flinksight.backend.repository.projection.ClusterStatusTrendProjection;
import com.flinksight.common.dto.*;
import com.flinksight.common.enums.AlertLevelEnum;
import com.flinksight.common.enums.ClusterHealthStatusEnum;
import com.flinksight.common.enums.JobStatusEnum;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DashboardService;
import com.flinksight.common.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 大盘统计聚合服务实现
 *
 * 所有数据均通过Repository多表聚合统计，严格多租户隔离，数据统计真实可靠。
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ClusterRepository clusterRepository;
    private final ClusterStatusHistoryRepository statusHistoryRepository;
    private final JobRepository jobRepository;
    private final AlertRepository alertRepository;
    private final JobMetricRepository jobMetricRepository;
    private final AlertHistoryRepository alertHistoryRepository;
    private final JobAlertLogRepository jobAlertLogRepository;
    private final UserRepository userRepository;



    /**
     * 获取大盘核心统计数据
     * @param tenantId 租户ID
     * @return 大盘核心统计DTO
     */
    @Override
    @Transactional(readOnly = true)
    public KPIStatusSummaryDTO getDashboardSummary(Long tenantId) {
        int clusterCount = clusterRepository.countByTenantIdAndIsDeleted(tenantId, 0);
        int jobCount = jobRepository.countByTenantIdAndIsDeleted(tenantId, 0);
        int alertCount = alertRepository.countByTenantIdAndIsDeleted(tenantId, 0);
        int activeJobCount = jobRepository.countByTenantIdAndStatusAndIsDeleted(
                tenantId, JobStatusEnum.RUNNING.getCode(), 0);
        long userCount = userRepository.countByTenantIdAndIsDeleted(tenantId,0);

        // 业务自定义：如健康度为健康集群占比
        int healthyCount = clusterRepository.countByTenantIdAndStatusAndIsDeleted(
                tenantId, ClusterHealthStatusEnum.HEALTHY.getCode(), 0);
        int healthScore = clusterCount > 0 ? healthyCount * 100 / clusterCount : 100;

        String statTime = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DEFAULT_FORMAT));

        return KPIStatusSummaryDTO.builder()
                .clusterCount(clusterCount)
                .jobCount(jobCount)
                .alertCount(alertCount)
                .userCount(userCount)
                .activeJobCount(activeJobCount)
                .healthScore(healthScore)
                .statTime(statTime)
                .build();
    }

    /**
     * 获取集群健康分布统计
     * @param tenantId 租户ID
     * @return 集群健康分布DTO
     */
    @Override
    @Transactional(readOnly = true)
    public HealthDistributionDTO getHealthDistribution(Long tenantId) {
        int healthyCount = clusterRepository.countByTenantIdAndStatusAndIsDeleted(
                tenantId, ClusterHealthStatusEnum.HEALTHY.getCode(), 0);
        int warningCount = clusterRepository.countByTenantIdAndStatusAndIsDeleted(
                tenantId, ClusterHealthStatusEnum.WARNING.getCode(), 0);
        int errorCount = clusterRepository.countByTenantIdAndStatusAndIsDeleted(
                tenantId, ClusterHealthStatusEnum.ERROR.getCode(), 0);

        String statTime = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return HealthDistributionDTO.builder()
                .healthyCount(healthyCount)
                .warningCount(warningCount)
                .errorCount(errorCount)
                .statTime(statTime)
                .build();
    }


    @Override
    public AlertTrendDTO getAlertTrend(Long tenantId, LocalDate from, LocalDate to) {
        // 归整为当天 00:00 - 当天 23:59:59
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);

        List<Alert> list = alertRepository.findTrend(tenantId, start, end);

        // 分天聚合
        Map<LocalDate, List<Alert>> byDay = list.stream()
                .collect(Collectors.groupingBy(a -> a.getCreatedAt().toLocalDate()));

        List<LocalDate> days = from.datesUntil(to.plusDays(1)).collect(Collectors.toList());

        List<String> times = new ArrayList<>();
        List<Long> total = new ArrayList<>();
        List<Long> fatal = new ArrayList<>();
        List<Long> warn = new ArrayList<>();

        for (LocalDate d : days) {
            List<Alert> day = byDay.getOrDefault(d, Collections.emptyList());
            long t = day.size();
            long f = day.stream().filter(a -> a.getLevel() == AlertLevelEnum.HIGH.getCode()).count();
            long w = day.stream().filter(a -> a.getLevel() == AlertLevelEnum.LOW.getCode()).count();

            times.add(d.toString());
            total.add(t);
            fatal.add(f);
            warn.add(w);
        }
        return new AlertTrendDTO(times, total, fatal, warn);
    }

    /**
     * 获取业务转化漏斗数据，支持分页
     * @param tenantId 租户ID
     * @param page 当前页码（0起始）
     * @param size 每页大小
     * @return 分页的JobFunnelDTO结果
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult<JobFunnelDTO> getJobFunnel(Long tenantId, int page, int size) {
        // 1. 查询所有分组统计结果
        List<Object[]> statList = jobRepository.countJobByStatusGroup(tenantId,0);

        // 2. 组装JobFunnelDTO
        List<JobFunnelDTO> allStages = statList.stream()
                .map(row -> JobFunnelDTO.builder()
                        .stage((String) row[0])
                        .count(((Number) row[1]).intValue())
                        .stageDesc(getStageDesc((String) row[0]))
                        .build())
                .collect(Collectors.toList());

        // 3. 自定义排序
        List<JobStatusEnum> order = Arrays.asList(
                JobStatusEnum.CREATED,
                JobStatusEnum.RUNNING,
                JobStatusEnum.FAILED,
                JobStatusEnum.STOPPED,
                JobStatusEnum.RESTARTING,
                JobStatusEnum.UNKNOWN
        );
        allStages.sort(Comparator.comparingInt(o -> order.indexOf(o.getStage())));

        // 4. 计算转化率
        int prev = allStages.size() > 0 ? allStages.get(0).getCount() : 1;
        for (int i = 0; i < allStages.size(); i++) {
            int cur = allStages.get(i).getCount();
            String rate = prev > 0 ? String.format("%.1f%%", cur * 100.0 / prev) : "0%";
            allStages.get(i).setConversionRate(rate);
            prev = cur;
        }

        // 5. 分页
        int total = allStages.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<JobFunnelDTO> pageData = allStages.subList(fromIndex, toIndex);

        // 6. 返回你本地的PageResult（data/total/page/size）
        return new PageResult<>(pageData, total, page, size);
    }

    @Override
    public MonitorMetricsDTO getMonitorMetrics(Long tenantId) {
        LocalDateTime now = LocalDateTime.now();
        long alertCount = alertRepository.countByTenantIdAndCreatedAtBetween(tenantId, now.minusDays(1), now);
        long jobRunning = jobRepository.countByTenantIdAndStatusAndIsDeleted(tenantId, JobStatusEnum.RUNNING.getCode(),0);
        long clusterHealthy = clusterRepository.countByTenantIdAndStatusAndIsDeleted(tenantId, ClusterHealthStatusEnum.HEALTHY.getCode(),0);
        long userCount = userRepository.countByTenantIdAndIsDeleted(tenantId,0);
        return new MonitorMetricsDTO(alertCount, jobRunning, clusterHealthy, userCount);
    }

    @Override
    public MetricSeriesDTO getMetricSeries(Long tenantId, String metric, LocalDateTime from, LocalDateTime to) {
        List<JobMetricDTO> points = jobMetricRepository
                .findSeries(tenantId, metric, from, to);
        List<String> times = points.stream().map(p -> p.getTs().toString()).toList();
        List<Double> values = points.stream().map(JobMetricDTO::getValue).toList();
        return new MetricSeriesDTO(times, values);
    }

    @Override
    public List<Map<String, Object>> getAlertCountBySeverity(Long tenantId, LocalDateTime start, LocalDateTime end) {
        return alertHistoryRepository.countByLevelBetween(tenantId, start, end);
    }


    @Override
    public List<Map<String, Object>> getAlertCountByStatus(Long tenantId, LocalDateTime start, LocalDateTime end) {
        return alertHistoryRepository.countByStatusBetween(tenantId, start, end);
    }


    @Override
    public Double getAverageResponseSeconds(Long tenantId) {
        return alertHistoryRepository.averageResponseTimeSeconds(tenantId);
    }

    @Override
    public List<Map<String, Object>> getFailedJobAlertTrend(Long tenantId, LocalDateTime start, LocalDateTime end) {
        return jobAlertLogRepository.countFailedJobAlertTrend(tenantId, start, end);
    }

    @Override
    public ClusterHealthMetricsDTO getClusterHealthMetrics(Long tenantId) {
        // 查询集群
        List<Cluster> clusters = clusterRepository.findByTenantIdAndIsDeleted(tenantId, 0);
        int totalClusters = clusters.size();

        // 查询过去24小时的采集数据
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusHours(24);
        List<ClusterStatusHistory> histories = statusHistoryRepository
                .findByCollectTimeBetweenAndIsDeleted(yesterday, now, 0);

        int totalNodes = 0;
        double totalCpu = 0;
        double totalMem = 0;
        int count = histories.size();

        for (ClusterStatusHistory h : histories) {
            totalNodes += h.getActiveNodeCount();
            totalCpu += h.getCpuUsage() != null ? h.getCpuUsage() : 0.0;
            totalMem += h.getMemoryUsage() != null ? h.getMemoryUsage() : 0.0;
        }

        return ClusterHealthMetricsDTO.builder()
                .totalClusters(totalClusters)
                .totalActiveNodes(totalNodes)
                .avgCpuUsage(count == 0 ? 0 : totalCpu / count)
                .avgMemoryUsage(count == 0 ? 0 : totalMem / count)
                .statTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @Override
    public ClusterTrendDTO getClusterTrend(Long tenantId, LocalDateTime from, LocalDateTime to) {
        List<ClusterStatusTrendProjection> rawData =
                statusHistoryRepository.findClusterStatusTrend(tenantId, from, to);

        // 初始化 Map：date → status → count
        Map<String, Map<String, Integer>> map = new TreeMap<>();
        for (ClusterStatusTrendProjection row : rawData) {
            map.computeIfAbsent(row.getDay(), k -> new HashMap<>())
                    .put(row.getStatus(), row.getCnt());
        }

        List<String> times = new ArrayList<>(map.keySet());
        List<Integer> healthy = new ArrayList<>();
        List<Integer> warning = new ArrayList<>();
        List<Integer> critical = new ArrayList<>();

        for (String day : times) {
            Map<String, Integer> stat = map.getOrDefault(day, new HashMap<>());
            healthy.add(stat.getOrDefault("healthy", 0));
            warning.add(stat.getOrDefault("warning", 0));
            critical.add(stat.getOrDefault("critical", 0));
        }

        return ClusterTrendDTO.builder()
                .times(times)
                .healthy(healthy)
                .warning(warning)
                .critical(critical)
                .build();
    }

    /**
     * 通过 JobStatusEnum 获取业务阶段描述
     */
    private String getStageDesc(String stage) {
        try {
            // 支持传入字符串code或枚举名
            JobStatusEnum status = Arrays.stream(JobStatusEnum.values())
                    .filter(e -> e.name().equalsIgnoreCase(stage) || String.valueOf(e.getCode()).equals(stage))
                    .findFirst()
                    .orElse(JobStatusEnum.UNKNOWN);
            return status.getLabel();
        } catch (Exception e) {
            return "未知阶段";
        }
    }

}
