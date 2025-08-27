package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.Cluster;
import com.flinksight.backend.domain.ClusterStatusHistory;
import com.flinksight.backend.domain.Node;
import com.flinksight.backend.domain.NodeHealth;
import com.flinksight.backend.mapper.ClusterStructMapper;
import com.flinksight.backend.mapper.NodeHealthStructMapper;
import com.flinksight.backend.mapper.NodeStructMapper;
import com.flinksight.backend.repository.ClusterRepository;
import com.flinksight.backend.repository.ClusterStatusHistoryRepository;
import com.flinksight.backend.repository.NodeHealthRepository;
import com.flinksight.backend.repository.NodeRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.*;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ClusterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 集群业务实现
 * Cluster Service Impl
 */
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class ClusterServiceImpl implements ClusterService{

    private final ClusterRepository clusterRepository;
    private final ClusterStructMapper clusterStructMapper;
    private final NodeRepository nodeRepository;
    private final NodeStructMapper nodeStructMapper;
    private final NodeHealthRepository nodeHealthRepository;
    private final ClusterStatusHistoryRepository cshRepository;
    private final NodeHealthStructMapper nodeHealthStructMapper;

    @Override
    public ClusterDTO createCluster(ClusterDTO req) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        // 租户内重名校验（幂等/友好报错）
        if (clusterRepository.existsByNameAndTenantIdAndIsDeleted(req.getName(), tenantId, 0)) {
            throw new IllegalArgumentException("集群名称已存在: " + req.getName());
        }
        Cluster e = clusterStructMapper.toEntity(req);
        e.setTenantId(tenantId);
        e.setIsDeleted(0);
        e.setCreatedAt(LocalDateTime.now());
        return clusterStructMapper.toDTO(clusterRepository.save(e));
    }

    @Override
    public ClusterDTO updateCluster(Long id, ClusterDTO req) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        Cluster e = clusterRepository.findByIdAndTenantIdAndIsDeleted(id, tenantId, 0)
                .orElseThrow(() -> new IllegalArgumentException("集群不存在或不属于当前租户"));
        clusterStructMapper.mergeIgnoreNullAndBlank(req, e); // 仅更新非 null 字段
        return clusterStructMapper.toDTO(clusterRepository.save(e));
    }

    @Override
    public Optional<ClusterDTO> getClusterById(Long clusterId) {
        return clusterRepository.findById(clusterId).map(clusterStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<ClusterDTO> list(int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, Cluster.class); // 统一 1→0
        Page<Cluster> result = clusterRepository.findAllByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(), 0, pr);
        return PageHelpers.toPageResult(result, clusterStructMapper::toDTO, true); // 返回 1-ba
    }


    @Override
    public ClusterDTO setClusterEnable(Long id, boolean enable) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        Cluster e = clusterRepository.findByIdAndTenantIdAndIsDeleted(id, tenantId, 0)
                .orElseThrow(() -> new IllegalArgumentException("集群不存在或不属于当前租户"));
        e.setStatus(enable ? 1 : 0);     // 幂等：重复设置不报错
        return clusterStructMapper.toDTO(clusterRepository.save(e));
    }

    @Override
    public void setClusterEnableBatch(List<Long> ids, boolean enable) {
        if (ids == null || ids.isEmpty()) return;
        Long tenantId = SecurityUtil.getCurrentTenantId();
        List<Cluster> list = clusterRepository.findAllByIdInAndTenantIdAndIsDeleted(ids, tenantId, 0);
        if (list.isEmpty()) return;
        int s = enable ? 1 : 0;
        list.forEach(c -> c.setStatus(s));
        clusterRepository.saveAll(list);
    }


    @Override
    @Transactional
    public void batchAddNodes(BatchAddNodesRequestDTO req) {
        if (req.getNodes() == null || req.getNodes().isEmpty()) return;
        List<Node> saves = new ArrayList<>();
        for (NodeDTO n : req.getNodes()) {
            Node node = Node.builder()
                    .name(n.getName())
                    .type(n.getType())
                    .ip(n.getIp())
                    .status(n.getStatus() != null ? n.getStatus() : 1)
                    .clusterId(req.getClusterId())
                    .isDeleted(0)
                    .createTime(LocalDateTime.now())
                    .build();
            saves.add(node);
        }
        nodeRepository.saveAll(saves);
    }

    @Override
    public boolean sDelete(Long id) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        Cluster e = clusterRepository.findByIdAndTenantIdAndIsDeleted(id, tenantId, 0)
                .orElseThrow(() -> new IllegalArgumentException("集群不存在或不属于当前租户"));
        e.setIsDeleted(1);
        clusterRepository.save(e);
        return true;
    }


    /* ------------------ Node ------------------ */

    @Override
    public NodeDTO createNode(NodeDTO req) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        // 校验 cluster 归属租户
        Cluster cluster = clusterRepository.findByIdAndTenantIdAndIsDeleted(req.getClusterId(), tenantId, 0)
                .orElseThrow(() -> new IllegalArgumentException("集群不存在或不属于当前租户"));
        // ip 唯一（若 DB 约束已加，这里是友好提示）
        if (nodeRepository.existsByIpAndIsDeleted(req.getIp(), 0)) {
            throw new IllegalArgumentException("节点 IP 已存在: " + req.getIp());
        }
        Node e = nodeStructMapper.toEntity(req);
        e.setIsDeleted(0);
        e.setCreateTime(LocalDateTime.now());
        return nodeStructMapper.toDTO(nodeRepository.save(e));
    }

    @Override
    public List<NodeDTO> batchAddNodes(List<NodeDTO> reqs) {
        if (reqs == null || reqs.isEmpty()) return List.of();

        Long tenantId = SecurityUtil.getCurrentTenantId();
        // 按 cluster 分组校验租户
        Map<Long, Cluster> clusterCache = new HashMap<>();
        List<Node> toSave = new ArrayList<>();

        for (NodeDTO r : reqs) {
            Cluster cluster = clusterCache.computeIfAbsent(r.getClusterId(), id ->
                    clusterRepository.findByIdAndTenantIdAndIsDeleted(id, tenantId, 0)
                            .orElseThrow(() -> new IllegalArgumentException("集群不存在或不属于当前租户: " + id)));

            // 友好 ip 去重检测
            if (nodeRepository.existsByIpAndIsDeleted(r.getIp(), 0)) {
                throw new IllegalArgumentException("节点 IP 已存在: " + r.getIp());
            }
            Node e = nodeStructMapper.toEntity(r);
            e.setIsDeleted(0);
            e.setCreateTime(LocalDateTime.now());
            toSave.add(e);
        }

        try {
            return nodeRepository.saveAll(toSave).stream().map(nodeStructMapper::toDTO).toList();
        } catch (DataIntegrityViolationException ex) {
            // 覆盖 DB 唯一约束异常
            throw new IllegalArgumentException("批量新增节点失败：可能存在重复 IP 或名称", ex);
        }
    }

    @Override
    public PageResult<NodeDTO> getNodesByCluster(Long clusterId, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, Node.class); // 统一 1→0
        // 校验 cluster 归属
        clusterRepository.findByIdAndTenantIdAndIsDeleted(clusterId, SecurityUtil.getCurrentTenantId(), 0)
                .orElseThrow(() -> new IllegalArgumentException("集群不存在或不属于当前租户"));
        Page<Node> result = nodeRepository.findByClusterIdAndIsDeleted(clusterId, 0, pr);
        return PageHelpers.toPageResult(result, nodeStructMapper::toDTO, true); // 返回 1-ba
    }


    @Override
    public NodeDTO setNodeEnable(Long nodeId, boolean enable) {
        // Node 没有 tenantId 字段，通过 Node -> Cluster 校验租户
        Node node = nodeRepository.findByIdAndIsDeleted(nodeId, 0)
                .orElseThrow(() -> new IllegalArgumentException("节点不存在"));
        Long tenantId = SecurityUtil.getCurrentTenantId();
        clusterRepository.findByIdAndTenantIdAndIsDeleted(node.getClusterId(), tenantId, 0)
                .orElseThrow(() -> new IllegalArgumentException("节点不属于当前租户"));
        node.setStatus(enable ? 1 : 0);
        return nodeStructMapper.toDTO(nodeRepository.save(node));
    }

    @Override
    public void setNodeEnableBatch(List<Long> nodeIds, boolean enable) {
        if (nodeIds == null || nodeIds.isEmpty()) return;
        Long tenantId = SecurityUtil.getCurrentTenantId();
        List<Node> nodes = nodeRepository.findAllByIdInAndIsDeleted(nodeIds, 0);
        if (nodes.isEmpty()) return;
        // 逐个校验归属（安全优先；若性能敏感，可改为 JOIN 查询）
        Set<Long> clusterIds = new HashSet<>();
        nodes.forEach(n -> clusterIds.add(n.getClusterId()));
        for (Long cid : clusterIds) {
            clusterRepository.findByIdAndTenantIdAndIsDeleted(cid, tenantId, 0)
                    .orElseThrow(() -> new IllegalArgumentException("包含不属于当前租户的节点"));
        }
        int s = enable ? 1 : 0;
        nodes.forEach(n -> n.setStatus(s));
        nodeRepository.saveAll(nodes);
    }


    /* ------------------ Health / Metrics ------------------ */

    @Override
    public PageResult<NodeHealthDTO> getNodeHealth(Long nodeId, int page,int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, NodeHealth.class); // 统一 1→0
        Page<NodeHealth> result = nodeHealthRepository
                .findByTenantIdAndNodeIdAndIsDeleted(SecurityUtil.getCurrentTenantId(), nodeId, 0, pr);
        return PageHelpers.toPageResult(result, nodeHealthStructMapper::toDTO, true); // 返回 1-ba
    }

    @Override
    public NodeMetricDTO getNodeMetric(Long clusterId, LocalDateTime from, LocalDateTime to) {
        // 若给定时间范围，则可返回某种聚合；这里给出“最近一次采集”的最通用实现
        if (from != null && to != null && from.isBefore(to)) {
            // 也可返回区间内的平均/最大值，按需扩展
            List<ClusterStatusHistory> list =
                    cshRepository.findByClusterIdAndCollectTimeBetweenAndIsDeleted(clusterId, from, to, 0);
            if (list.isEmpty()) return null;
            ClusterStatusHistory last = list.stream()
                    .max(Comparator.comparing(ClusterStatusHistory::getCollectTime)).get();
            return toMetricDTO(last);
        } else {
            return cshRepository.findTopByClusterIdAndIsDeletedOrderByCollectTimeDesc(clusterId, 0)
                    .map(this::toMetricDTO)
                    .orElse(null);
        }
    }

    private NodeMetricDTO toMetricDTO(ClusterStatusHistory h) {
        NodeMetricDTO dto = new NodeMetricDTO();
        dto.setActiveNodeCount(h.getActiveNodeCount());
        dto.setCpuUsage(h.getCpuUsage());
        dto.setMemoryUsage(h.getMemoryUsage());
        dto.setQueueLoadJson(h.getQueueLoadJson());
        dto.setCollectTime(h.getCollectTime());
        return dto;
    }

}
