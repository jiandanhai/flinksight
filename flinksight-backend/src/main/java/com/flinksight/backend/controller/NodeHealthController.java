package com.flinksight.backend.controller;

import com.flinksight.backend.domain.NodeHealth;
import com.flinksight.common.dto.NodeHealthDTO;
import com.flinksight.common.service.NodeHealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 节点健康管理
 */
@RestController
@RequestMapping("/api/node-health")
@RequiredArgsConstructor
public class NodeHealthController {

    private final NodeHealthService service;

    @GetMapping("/{id}")
    public Optional<NodeHealthDTO> get(@PathVariable Long id) {
        return service.getLatestByNodeId(id);
    }

    @GetMapping("/node/{nodeId}")
    public List<NodeHealthDTO> findByNodeId(@PathVariable Long nodeId) {
        return service.getByNodeId(nodeId);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
