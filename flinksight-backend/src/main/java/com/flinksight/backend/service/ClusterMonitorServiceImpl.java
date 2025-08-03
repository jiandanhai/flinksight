package com.flinksight.backend.service;

import com.flinksight.backend.domain.ClusterStatusHistory;
import com.flinksight.backend.mapper.ClusterStatusHistoryStructMapper;
import com.flinksight.backend.mapper.ClusterStructMapper;
import com.flinksight.backend.repository.ClusterRepository;
import com.flinksight.backend.repository.ClusterStatusHistoryRepository;
import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.dto.ClusterStatusHistoryDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ClusterMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClusterMonitorServiceImpl implements ClusterMonitorService {

    private final ClusterRepository clusterRepo;
    private final ClusterStatusHistoryRepository statusRepo;
    private final ClusterStructMapper clusterStructMapper;
    private final ClusterStatusHistoryStructMapper clusterStatusHistoryStructMapper;

    /**
     * 每5分钟采集一次所有集群状态，可用@Scheduled(fixedDelay = 300000)
     */
    @Override
    @Scheduled(fixedDelay = 300000)
    public void collectAllClusterStatus() {
        List<ClusterDTO> clusters  = clusterStructMapper.toDTOList(clusterRepo.findByStatusAndIsDeleted(1,0));
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
        statusRepo.save(clusterStatusHistoryStructMapper.toEntity(dto));
        return dto;
    }

    @Override
    public PageResult<ClusterStatusHistoryDTO> getHistory(Long clusterId,int page, int size) {
        Page<ClusterStatusHistory> result = statusRepo.findByClusterIdAndIsDeletedOrderByCollectTimeDesc(clusterId, 0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<ClusterStatusHistoryDTO> dtoPage = result.map(clusterStatusHistoryStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }
}
