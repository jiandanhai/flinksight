package com.flinksight.backend.controller;

import com.flinksight.backend.domain.Node;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 节点管理
 */
@RestController
@RequestMapping("/api/node")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService service;

    @PostMapping
    public NodeDTO create(@RequestBody NodeDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<NodeDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<NodeDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/cluster/{clusterId}")
    public List<NodeDTO> findByClusterId(@PathVariable Long clusterId) {
        return service.findByClusterId(clusterId);
    }

    @PutMapping
    public NodeDTO update(@RequestBody NodeDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
