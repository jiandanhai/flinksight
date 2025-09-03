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
import com.flinksight.common.enums.NodeState;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.ClusterService;
import com.flinksight.common.service.cluster.probe.ClusterHealthProbe;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.HttpStatus.*;

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
    private final List<ClusterHealthProbe> probes; // Spring 会自动注入所有实现

    @Override
    public ClusterDTO update(Long id, ClusterDTO req) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        Cluster e = clusterRepository.findByIdAndTenantIdAndIsDeleted(id, tenantId, 0)
                .orElseThrow(() -> new IllegalArgumentException("集群不存在或不属于当前租户"));
        clusterStructMapper.mergeIgnoreNullAndBlank(req, e); // 仅更新非 null 字段
        return clusterStructMapper.toDTO(clusterRepository.save(e));
    }


    @Override
    public Optional<ClusterDTO> get(Long clusterId) {
        return clusterRepository.findById(clusterId).map(clusterStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<ClusterDTO> list(String type, Integer status, String q, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, Cluster.class); // 统一 1→0
        Page<Cluster> result = clusterRepository.search(SecurityUtil.getCurrentTenantId(),
                type!=null? type.toUpperCase(Locale.ROOT): null, status, q, pr);
        return PageHelpers.toPageResult(result, clusterStructMapper::toDTO, true); // 返回 1-ba
    }

    @Transactional
    @Override
    public void changeStatus(Long id, Integer status) {
        if (status == null || (status!=0 && status!=1)) {
            throw new ResponseStatusException(BAD_REQUEST, "status 只能为 0/1");
        }
        Cluster c = load(id);
        c.setStatus(status);
        clusterRepository.save(c);
    }

    @Override public ClusterHealthDTO healthById(Long id) {
        Cluster c = load(id);
        try {
            return probe(c.getType()).check(clusterStructMapper.toDTO(c));
        } catch (Exception e) {
            return ClusterHealthDTO.builder()
                    .name(c.getName()).type(c.getType()).endpoint(c.getEndpoint())
                    .status(ClusterHealthDTO.Status.DOWN).latencyMs(0L)
                    .message("健康检查异常: "+e.getMessage())
                    .samples(java.util.Map.of())
                    .checkedAt(java.time.Instant.now())
                    .build();
        }
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
                    .status(toNodeState(n.getStatus()))
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
        e.setStatus(NodeState.ENABLED);   // 例如新增节点默认启用
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
            e.setStatus(NodeState.ENABLED);   // 例如新增节点默认启用
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
        node.setStatus(enable ? NodeState.ENABLED : NodeState.DISABLED);
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
        nodes.forEach(n -> n.setStatus(enable ? NodeState.ENABLED : NodeState.DISABLED));
        nodeRepository.saveAll(nodes);
    }


    /* ------------------ Health / Metrics ------------------ */

    @Override
    public PageResult<NodeHealthDTO> listNodeHealthRecords(Long nodeId, int page,int size) {
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

    // 放在本类里（private static 也可以）
    private NodeState toNodeState(Integer v) {
        // 你的 NodeState 是 code="0"/"1"，null 时给默认值
        return v == null
                ? NodeState.ENABLED
                : NodeState.ofCodeOrDefault(String.valueOf(v), NodeState.ENABLED);
        // 等价写法（不依赖 ofCode）：
        // return v != null && v == 1 ? NodeState.DISABLED : NodeState.ENABLED;
    }


    @Transactional
    @Override
    public ClusterDTO register(ClusterSpecDTO spec) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        // 3) 名称在租户下唯一
        if (clusterRepository.existsByTenantIdAndName(tenantId, spec.getName())) {
            throw new ResponseStatusException(CONFLICT, "集群名已存在：" + spec.getName());
        }

        // 4) 可选：连通性预检查（根据类型尝试一次探针）
        String normalizedType = spec.getType().toUpperCase(Locale.ROOT);
        Cluster preview = Cluster.builder()
                .tenantId(tenantId)
                .name(spec.getName())
                .type(normalizedType)
                .endpoint(spec.getEndpoint())
                .version(spec.getVersion())
                .tags(spec.getTags())
                .status(1)
                .remark(spec.getRemark())
                .isDeleted(0)
                .createdAt(LocalDateTime.now())
                .build();

        // 若你希望注册时就校验联通性，可以打开下面代码；否则仅保存，由健康页再测
        // try {
        //   ClusterHealthDTO health = pickProbe(normalizedType).check(preview);
        //   if (health.getStatus() == ClusterHealthDTO.Status.DOWN) {
        //     throw new ResponseStatusException(BAD_REQUEST, "集群不可达：" + health.getMessage());
        //   }
        //   // 自动回填版本
        //   if (preview.getVersion() == null && health.getSamples() != null) {
        //     Object v = health.getSamples().get("flink.version");
        //     if (v instanceof String vs && !vs.isBlank()) preview.setVersion(vs);
        //   }
        // } catch (Exception e) {
        //   throw new ResponseStatusException(BAD_REQUEST, "连通性校验失败：" + e.getMessage());
        // }

        // 5) 落库
        Cluster saved = clusterRepository.save(preview);
        return ClusterDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .type(saved.getType())
                .endpoint(saved.getEndpoint())
                .version(saved.getVersion())
                .tags(saved.getTags())
                .status(saved.getStatus())
                .remark(saved.getRemark())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    public ClusterHealthDTO healthByName(String name) {

        Cluster c = clusterRepository.findByTenantIdAndName(SecurityUtil.getCurrentTenantId(), name)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "未找到该集群：" + name));
        try {
            return pickProbe(c.getType()).check(clusterStructMapper.toDTO(c));
        } catch (ResponseStatusException rse) {
            throw rse;
        } catch (Exception e) {
            // 任何异常都转为 DOWN，避免把栈抛给前端
            return ClusterHealthDTO.builder()
                    .name(c.getName())
                    .type(c.getType())
                    .endpoint(c.getEndpoint())
                    .status(ClusterHealthDTO.Status.DOWN)
                    .latencyMs(0)
                    .message("健康检查异常：" + e.getMessage())
                    .samples(java.util.Map.of())
                    .checkedAt(java.time.Instant.now())
                    .build();
        }
    }

    // ---------- helpers ----------

    private ClusterHealthProbe pickProbe(String type) {
        return probes.stream()
                .filter(p -> p.supports(type))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "不支持的集群类型：" + type));
    }

    private Cluster load(Long id) {
        return clusterRepository.findByTenantIdAndId(SecurityUtil.getCurrentTenantId(), id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "集群不存在或无权访问"));
    }
    private ClusterDTO toDTO(Cluster c) {
        return ClusterDTO.builder()
                .id(c.getId()).name(c.getName()).type(c.getType())
                .endpoint(c.getEndpoint()).version(c.getVersion())
                .tags(c.getTags()).status(c.getStatus())
                .remark(c.getRemark()).createdAt(c.getCreatedAt())
                .build();
    }
    private ClusterHealthProbe probe(String type){
        return probes.stream().filter(p -> p.supports(type)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "不支持的集群类型: "+type));
    }

}
