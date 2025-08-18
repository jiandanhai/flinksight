package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.JobInfoDTO;
import com.flinksight.common.dto.JobRegisterRequestDTO;
import com.flinksight.common.service.JobRegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作业自动注册API
 * 路径：POST /api/job/register
 */
@Tag(name = "api", description = "Job作业自动注册API")
@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
@Validated
public class JobRegisterController {

    private final JobRegisterService jobRegisterService;

    /**
     * 自动注册作业，平台幂等/权限校验/多租户
     */
    @Operation(summary = "", description = "",operationId = "registerJob")
    @PostMapping("/register")
    public ApiResponse<JobInfoDTO> registerJob(@RequestBody @Valid JobRegisterRequestDTO req) {
        // （建议接口层可加租户/平台黑白名单防刷）
        JobInfoDTO job = jobRegisterService.register(req);
        return ApiResponse.ok(job);
    }
}
