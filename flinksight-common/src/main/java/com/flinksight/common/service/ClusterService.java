package com.flinksight.common.service;

import com.flinksight.common.dto.ClusterDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

/**
 * 集群业务接口
 * Cluster Service
 */
public interface ClusterService extends SoftDeleteService<ClusterDTO, Long>  {
    ClusterDTO createOrUpdate(ClusterDTO clusterDTO);
    Optional<ClusterDTO> getClusterById(Long clusterId);
    PageResult<ClusterDTO> getClustersByTenant(Long tenantId,int page, int size);
}
