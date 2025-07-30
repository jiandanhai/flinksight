package com.flinksight.backend.controller;

import com.flinksight.backend.domain.SysParam;
import com.flinksight.common.dto.SysParamDTO;
import com.flinksight.common.service.SysParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 系统参数管理
 */
@RestController
@RequestMapping("/api/sys-param")
@RequiredArgsConstructor
public class SysParamController {

    private final SysParamService service;

    @PostMapping
    public SysParamDTO create(@RequestBody SysParamDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<SysParamDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<SysParamDTO> getAll() {
        return service.getAll();
    }

    @PutMapping
    public SysParamDTO update(@RequestBody SysParamDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
