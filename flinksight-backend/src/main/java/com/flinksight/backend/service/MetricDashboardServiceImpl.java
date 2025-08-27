package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.ClusterStatusHistory;
import com.flinksight.backend.domain.MetricDashboard;
import com.flinksight.backend.mapper.ClusterStatusHistoryStructMapper;
import com.flinksight.backend.mapper.MetricDashboardStructMapper;
import com.flinksight.backend.repository.ClusterStatusHistoryRepository;
import com.flinksight.backend.repository.MetricDashboardRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.MetricDashboardDTO;
import com.flinksight.common.dto.NodeMetricResponseDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.MetricDashboardService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MetricDashboardServiceImpl implements MetricDashboardService {
    private final MetricDashboardRepository repository;
    private final MetricDashboardStructMapper metricDashboardStructMapper;
    private final ClusterStatusHistoryRepository historyRepository;
    private final ClusterStatusHistoryStructMapper clusterStatusHistoryStructMapper;

    @Override
    public MetricDashboardDTO createOrUpdate(MetricDashboardDTO metricDashboardDTO) {
        MetricDashboard entity = metricDashboardStructMapper.toEntity(metricDashboardDTO);
        entity.setIsDeleted(0);
        MetricDashboard saved = repository.save(entity);
        return metricDashboardStructMapper.toDTO(saved);
    }

    @Override
    public Optional<MetricDashboardDTO> getById(Long id) {
        return repository.findById(id).map(metricDashboardStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }


    @Override
    public PageResult<MetricDashboardDTO> list(int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, MetricDashboard.class); // 统一 1→0
        Page<MetricDashboard> result = repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),0, pr);
        return PageHelpers.toPageResult(result, metricDashboardStructMapper::toDTO, true); // 返回
    }

    @Override
    public NodeMetricResponseDTO getNodeMetric(Long clusterId, LocalDateTime from, LocalDateTime to, String agg) {
        List<ClusterStatusHistory> raw = historyRepository
                .findAllByClusterIdAndCollectTimeBetweenOrderByCollectTime(clusterId, from, to);

        // 简单聚合：none/hour/day 三档（默认 none）
        Map<String, Bucket> buckets = new LinkedHashMap<>();
        for (ClusterStatusHistory h : raw) {
            String key = formatKey(h.getCollectTime(), agg);
            Bucket b = buckets.computeIfAbsent(key, k -> new Bucket());
            b.count++;
            if (h.getCpuUsage() != null) b.cpuSum += h.getCpuUsage();
            if (h.getMemoryUsage() != null) b.memSum += h.getMemoryUsage();
            if (h.getActiveNodeCount() != null) b.activeSum += h.getActiveNodeCount();
        }

        List<String> times = new ArrayList<>();
        List<Double> cpu = new ArrayList<>();
        List<Double> memory = new ArrayList<>();
        List<Integer> active = new ArrayList<>();
        for (Map.Entry<String, Bucket> e : buckets.entrySet()) {
            times.add(e.getKey());
            Bucket b = e.getValue();
            cpu.add(b.count == 0 ? 0d : round2(b.cpuSum / b.count));
            memory.add(b.count == 0 ? 0d : round2(b.memSum / b.count));
            active.add(b.count == 0 ? 0 : (int)Math.round(b.activeSum / b.count));
        }

        return NodeMetricResponseDTO.builder()
                .times(times)
                .cpu(cpu)
                .memory(memory)
                .activeNodes(active)
                .build();
    }

    private static class Bucket {
        int count = 0;
        double cpuSum = 0d;
        double memSum = 0d;
        double activeSum = 0d;
    }

    private String formatKey(LocalDateTime t, String agg) {
        if (agg == null) agg = "none";
        switch (agg.toLowerCase()) {
            case "hour":
                return t.withMinute(0).withSecond(0).withNano(0)
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            case "day":
                return t.toLocalDate().atStartOfDay()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            default:
                return t.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<MetricDashboardDTO> opt = repository.findById(id).map(metricDashboardStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            MetricDashboardDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(metricDashboardStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
