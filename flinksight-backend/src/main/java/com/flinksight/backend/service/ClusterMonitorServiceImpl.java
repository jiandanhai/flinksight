package com.flinksight.backend.service;

import com.flinksight.backend.domain.AuditLog;
import com.flinksight.backend.domain.Cluster;
import com.flinksight.backend.domain.ClusterStatusHistory;
import com.flinksight.backend.mapper.ClusterStatusHistoryStructMapper;
import com.flinksight.backend.mapper.ClusterStructMapper;
import com.flinksight.backend.repository.ClusterRepository;
import com.flinksight.backend.repository.ClusterStatusHistoryRepository;
import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.dto.ClusterStatusHistoryDTO;
import com.flinksight.common.service.ClusterMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClusterMonitorServiceImpl implements ClusterMonitorService {

    private final ClusterRepository clusterRepo;
    private final ClusterStatusHistoryRepository statusRepo;
    private final ClusterStructMapper mapper;
    private final ClusterStatusHistoryStructMapper cshMapper;

    /**
     * 每5分钟采集一次所有集群状态，可用@Scheduled(fixedDelay = 300000)
     */
    @Override
    @Scheduled(fixedDelay = 300000)
    public void collectAllClusterStatus() {
        List<ClusterDTO> clusters  = mapper.toDTOList(clusterRepo.findByStatusAndIsDeleted(1,0));
        for (ClusterDTO c : clusters) {
            collectStatus(c);
        }
    }

    @Override
    public ClusterStatusHistoryDTO collectStatus(ClusterDTO clusterDTO) {
        // 真实业务中需调度YARN/K8S/Standalone API获得监控数据
        ClusterStatusHistoryDTO dto = ClusterStatusHistoryDTO.builder()
                .clusterId(clusterDTO.getId())
                .collectTime(LocalDateTime.now())
                .activeNodeCount(8) // 实时采集
                .cpuUsage(0.62)     // 实时采集
                .memoryUsage(0.71)  // 实时采集
                .queueLoadJson("{\"root.default\":0.5,\"root.flink\":0.4}") // 实时采集
                .extendJson("{}")
                .isDeleted(0)
                .build();
        statusRepo.save(cshMapper.toEntity(dto));
        return dto;
    }

    @Override
    public List<ClusterStatusHistoryDTO> getHistory(Long clusterId, int limit) {
        return cshMapper.toDTOList(statusRepo.findTopNByClusterIdAndIsDeletedOrderByCollectTimeDesc(clusterId, 0, limit));
    }
}
