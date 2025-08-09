package com.flinksight.backend.service;

import com.flinksight.backend.repository.AlertRepository;
import com.flinksight.backend.repository.ClusterRepository;
import com.flinksight.backend.repository.JobRepository;
import com.flinksight.common.dto.DashboardSummaryDTO;
import com.flinksight.common.dto.HealthDistributionDTO;
import com.flinksight.common.dto.JobFunnelDTO;
import com.flinksight.common.enums.ClusterHealthStatusEnum;
import com.flinksight.common.enums.JobStatusEnum;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DashboardService;
import com.flinksight.common.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
    private final JobRepository jobRepository;
    private final AlertRepository alertRepository;

    /**
     * 获取大盘核心统计数据
     * @param tenantId 租户ID
     * @return 大盘核心统计DTO
     */
    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryDTO getDashboardSummary(Long tenantId) {
        int clusterCount = clusterRepository.countByTenantIdAndIsDeleted(tenantId, 0);
        int jobCount = jobRepository.countByTenantIdAndIsDeleted(tenantId, 0);
        int alertCount = alertRepository.countByTenantIdAndIsDeleted(tenantId, 0);
        int activeJobCount = jobRepository.countByTenantIdAndStatusAndIsDeleted(
                tenantId, JobStatusEnum.RUNNING.getCode(), 0);

        // 业务自定义：如健康度为健康集群占比
        int healthyCount = clusterRepository.countByTenantIdAndStatusAndIsDeleted(
                tenantId, ClusterHealthStatusEnum.HEALTHY.getCode(), 0);
        int healthScore = clusterCount > 0 ? healthyCount * 100 / clusterCount : 100;

        String statTime = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DEFAULT_FORMAT));

        return DashboardSummaryDTO.builder()
                .clusterCount(clusterCount)
                .jobCount(jobCount)
                .alertCount(alertCount)
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
        List<Object[]> statList = jobRepository.countJobByStatusGroup(tenantId, 0);

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
