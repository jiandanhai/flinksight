package com.flinksight.common.service;

import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.dto.ClusterStatusHistoryDTO;

import java.util.List;

public interface ClusterMonitorService {
    /**
     * 对所有集群采集状态
     */
    void collectAllClusterStatus();

    /**
     * 采集指定集群状态
     */
    ClusterStatusHistoryDTO collectStatus(ClusterDTO cluster);

    /**
     * 查询集群历史状态
     */
    List<ClusterStatusHistoryDTO> getHistory(Long clusterId, int limit);
}
