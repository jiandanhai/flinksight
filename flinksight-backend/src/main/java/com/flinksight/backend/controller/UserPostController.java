package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.UserPostDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户-岗位分配管理
 */
@RestController
@RequestMapping("/api/user-post")
@RequiredArgsConstructor
public class UserPostController {

    private final UserPostService service;

    @PostMapping("/assign")
    public ApiResponse<UserPostDTO> assign(@RequestParam Long userId, @RequestParam Long postId) {
        return ApiResponse.ok(service.assignPostToUser(userId, postId));
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long postId) {
        return service.removePostFromUser(userId, postId);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<PageResult<UserPostDTO>> findByUser(@PathVariable Long userId,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findByUserId(userId,page,size));
    }

    @GetMapping("/post/{postId}")
    public ApiResponse<PageResult<UserPostDTO>> findByPost(@PathVariable Long postId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {

        return ApiResponse.ok(service.findByPostId(postId,page,size));
    }

    @GetMapping("/{id}")
    public ApiResponse<UserPostDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
