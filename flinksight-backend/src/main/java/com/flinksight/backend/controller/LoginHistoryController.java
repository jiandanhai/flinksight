package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.LoginHistoryDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.LoginHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 登录历史管理
 */
@Tag(name = "api", description = "登录历史管理API")
@RestController
@RequestMapping("/api/login-history")
@RequiredArgsConstructor
@Validated
public class LoginHistoryController {

    private final LoginHistoryService service;

    @Operation(summary = "", description = "",operationId = "createLoginHistory")
    @PostMapping
    public ApiResponse<LoginHistoryDTO> create(@RequestBody @Valid LoginHistoryDTO dto) {

        return ApiResponse.ok(service.create(dto));
    }

    @Operation(summary = "", description = "",operationId = "getLoginHistory")
    @GetMapping("/id/{id}")
    public ApiResponse<LoginHistoryDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "listLoginHistories")
    @GetMapping("/list")
    public ApiResponse<PageResult<LoginHistoryDTO>> list(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.list(userId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "deleteLoginHistory")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }

    @Operation(summary = "统计用户成功登录次数",operationId = "countSuccessLoginByUser")
    @GetMapping("/count-success")
    public ApiResponse<Long> countUserSuccess(@RequestParam Long userId) {
        return ApiResponse.ok(service.countUserSuccessLogin(userId));
    }
}
