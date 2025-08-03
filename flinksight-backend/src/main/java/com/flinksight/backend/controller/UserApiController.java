package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserApiDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-API权限分配管理
 */
@RestController
@RequestMapping("/api/user-api")
@RequiredArgsConstructor
public class UserApiController {

    private final UserApiService service;

    @PostMapping("/assign")
    public ApiResponse<UserApiDTO> assign(@RequestParam Long userId, @RequestParam Long apiId) {
        return ApiResponse.ok(service.assignApiToUser(userId, apiId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long apiId) {
        return service.removeApiFromUser(userId, apiId);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserApiDTO>> findByUser(@PathVariable Long userId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @GetMapping("/api/{apiId}")
    public ApiResponse<PageResult<UserApiDTO>> findByApi(@PathVariable Long apiId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.findByApiId(apiId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserApiDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
