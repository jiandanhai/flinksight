package com.flinksight.backend.controller;

import com.flinksight.backend.domain.ApiWhitelist;
import com.flinksight.common.dto.ApiWhitelistDTO;
import com.flinksight.common.service.ApiWhitelistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * 接口白名单管理
 */
@RestController
@RequestMapping("/api/api-whitelist")
@RequiredArgsConstructor
public class ApiWhitelistController {

    private final ApiWhitelistService service;

    @PostMapping
    public ApiWhitelistDTO create(@RequestBody ApiWhitelistDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<ApiWhitelistDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping
    public ApiWhitelistDTO update(@RequestBody ApiWhitelistDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
