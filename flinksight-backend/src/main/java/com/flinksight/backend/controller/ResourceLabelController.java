package com.flinksight.backend.controller;

import com.flinksight.backend.domain.ResourceLabel;
import com.flinksight.common.dto.ResourceLabelDTO;
import com.flinksight.common.service.ResourceLabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 资源-标签关联管理
 */
@RestController
@RequestMapping("/api/resource-label")
@RequiredArgsConstructor
public class ResourceLabelController {

    private final ResourceLabelService service;

    @PostMapping("/assign")
    public ResourceLabelDTO assign(@RequestParam Long resourceId, @RequestParam Long labelId) {
        return service.assignLabelToResource(resourceId, labelId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long resourceId, @RequestParam Long labelId) {
        return service.removeLabelFromResource(resourceId, labelId);
    }

    @GetMapping("/resource/{resourceId}")
    public List<ResourceLabelDTO> findByResourceId(@PathVariable Long resourceId) {
        return service.findByResourceId(resourceId);
    }

    @GetMapping("/label/{labelId}")
    public List<ResourceLabelDTO> findByLabelId(@PathVariable Long labelId) {
        return service.findByLabelId(labelId);
    }

    @GetMapping("/{id}")
    public Optional<ResourceLabelDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
