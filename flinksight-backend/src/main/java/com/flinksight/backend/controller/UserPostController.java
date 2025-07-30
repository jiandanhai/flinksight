package com.flinksight.backend.controller;

import com.flinksight.backend.domain.UserPost;
import com.flinksight.common.dto.UserPostDTO;
import com.flinksight.common.service.UserPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 用户-岗位分配管理
 */
@RestController
@RequestMapping("/api/user-post")
@RequiredArgsConstructor
public class UserPostController {

    private final UserPostService service;

    @PostMapping("/assign")
    public UserPostDTO assign(@RequestParam Long userId, @RequestParam Long postId) {
        return service.assignPostToUser(userId, postId);
    }

    @PostMapping("/remove")
    public boolean remove(@RequestParam Long userId, @RequestParam Long postId) {
        return service.removePostFromUser(userId, postId);
    }

    @GetMapping("/user/{userId}")
    public List<UserPostDTO> findByUser(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/post/{postId}")
    public List<UserPostDTO> findByPost(@PathVariable Long postId) {
        return service.findByPostId(postId);
    }

    @GetMapping("/{id}")
    public Optional<UserPostDTO> getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
