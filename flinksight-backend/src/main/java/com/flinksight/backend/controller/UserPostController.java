package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserPostDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-岗位分配管理
 */
@Tag(name = "api", description = "用户-岗位分配管理API")
@RestController
@RequestMapping("/api/user/post")
@RequiredArgsConstructor
@Validated
public class UserPostController {

    private final UserPostService service;

    @Operation(summary = "", description = "",operationId = "assignUserPost")
    @PostMapping("/assign")
    public ApiResponse<UserPostDTO> assign(@RequestParam Long userId, @RequestParam Long postId) {
        return ApiResponse.ok(service.assignPostToUser(userId, postId));
    }

    @Operation(summary = "", description = "",operationId = "removeUserPost")
    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long postId) {
        return service.removePostFromUser(userId, postId);
    }

    @Operation(summary = "", description = "",operationId = "getUserPostsByUser")
    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserPostDTO>> findByUser(@PathVariable Long userId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserPostsByPost")
    @GetMapping("/post/{postId}")
    public ApiResponse<PageResult<UserPostDTO>> findByPost(@PathVariable Long postId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findByPostId(postId,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getUserPost")
    @GetMapping("/id/{id}")
    public ApiResponse<UserPostDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteUserPost")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
