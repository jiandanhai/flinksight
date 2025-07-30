package com.flinksight.backend.controller;

import com.flinksight.backend.domain.Profile;
import com.flinksight.common.dto.ProfileDTO;
import com.flinksight.common.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * 用户扩展档案管理
 */
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;

    @PostMapping
    public ProfileDTO create(@RequestBody ProfileDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<ProfileDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/user/{userId}")
    public ProfileDTO getByUserId(@PathVariable Long userId) {
        return service.getByUserId(userId);
    }

    @PutMapping
    public ProfileDTO update(@RequestBody ProfileDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
