package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.JobInstanceDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 作业实例管理API
 */
@RestController
@Tag(name = "api", description = "作业实例管理API")
@RequestMapping("/api/job/instance")
@RequiredArgsConstructor
@Validated
public class JobInstanceController {

    private final JobInstanceService service;

    @Operation(summary = "", description = "",operationId = "createJobInstance")
    @PostMapping("/create")
    public ApiResponse<JobInstanceDTO> create(@RequestBody @Valid JobInstanceDTO dto) {

        return ApiResponse.ok(service.createJob(dto));
    }

    @Operation(summary = "", description = "",operationId = "updateJobInstance")
    @PostMapping("/update-status")
    public boolean updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        return service.updateJobStatus(id, status);
    }

    @Operation(summary = "", description = "",operationId = "listJobInstances")
    @GetMapping("/status/{status}")
    public ApiResponse<PageResult<JobInstanceDTO>> list(
            @PathVariable Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.list(status,page,size));
    }

    @Operation(summary = "", description = "",operationId = "getJobInstance")
    @GetMapping("/id/{id}")
    public ApiResponse<JobInstanceDTO> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.ok(null));
    }

    @Operation(summary = "", description = "",operationId = "deleteJobInstance")
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.sDelete(id);
    }
}
