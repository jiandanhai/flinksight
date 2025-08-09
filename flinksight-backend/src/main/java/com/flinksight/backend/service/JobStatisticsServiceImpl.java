package com.flinksight.backend.service;

import com.flinksight.backend.repository.JobRepository;
import com.flinksight.common.dto.JobFunnelDTO;
import com.flinksight.common.enums.JobStatusEnum;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Job统计聚合实现
 */
@Service
@RequiredArgsConstructor
public class JobStatisticsServiceImpl implements JobStatisticsService {

    private final JobRepository jobRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResult<JobFunnelDTO> getJobFunnel(Long tenantId, int page, int size) {
        List<Object[]> statList = jobRepository.countJobByStatusGroup(tenantId, 0);
        List<JobFunnelDTO> allStages = statList.stream()
                .map(row -> JobFunnelDTO.builder()
                        .stage((String) row[0])
                        .count(((Number) row[1]).intValue())
                        .stageDesc(getStageDesc((String) row[0]))
                        .build())
                .collect(Collectors.toList());

        // 自定义排序（按业务阶段顺序）
        List<JobStatusEnum> order = Arrays.asList(
                JobStatusEnum.CREATED,
                JobStatusEnum.RUNNING,
                JobStatusEnum.FAILED,
                JobStatusEnum.STOPPED,
                JobStatusEnum.RESTARTING,
                JobStatusEnum.UNKNOWN
        );
        allStages.sort(Comparator.comparingInt(o -> order.indexOf(o.getStage())));

        // 计算转化率
        int prev = allStages.size() > 0 ? allStages.get(0).getCount() : 1;
        for (int i = 0; i < allStages.size(); i++) {
            int cur = allStages.get(i).getCount();
            String rate = prev > 0 ? String.format("%.1f%%", cur * 100.0 / prev) : "0%";
            allStages.get(i).setConversionRate(rate);
            prev = cur;
        }

        int total = allStages.size();
        int from = Math.min(page * size, total);
        int to = Math.min(from + size, total);
        List<JobFunnelDTO> pageList = allStages.subList(from, to);

        return new PageResult<>(pageList, total, page, size);
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
