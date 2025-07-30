package com.flinksight.backend.controller;

import com.flinksight.backend.domain.LoginHistory;
import com.flinksight.common.dto.LoginHistoryDTO;
import com.flinksight.common.service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 登录历史管理
 */
@RestController
@RequestMapping("/api/login-history")
@RequiredArgsConstructor
public class LoginHistoryController {

    private final LoginHistoryService service;

    @PostMapping
    public LoginHistoryDTO create(@RequestBody LoginHistoryDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public Optional<LoginHistoryDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/user/{userId}")
    public List<LoginHistoryDTO> findByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
