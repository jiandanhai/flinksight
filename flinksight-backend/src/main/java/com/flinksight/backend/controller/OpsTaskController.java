package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.OpsTaskDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.OpsTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 运维自动化任务管理控制器
 * 提供运维任务的RESTful API入口
 */
@Tag(name = "api",description = "运维自动化任务管理控制器API")
@RestController
@RequestMapping("/api/ops-task")
@RequiredArgsConstructor
@Validated
public class OpsTaskController {

    private final OpsTaskService opsTaskService;

    /**
     * 新建运维任务
     * @param dto 运维任务参数
     * @return 新建后的任务信息
     */
    @Operation(summary = "", description = "", operationId = "createOpsTask")
    @PostMapping("/create")
    public ApiResponse<OpsTaskDTO> create(@Valid @RequestBody OpsTaskDTO dto) {
        return ApiResponse.ok(opsTaskService.create(dto));
    }

    /**
     * 更新运维任务
     * @param dto 运维任务参数
     * @return 更新后的任务信息
     */
    @Operation(summary = "", description = "", operationId = "updateOpsTask")
    @PostMapping("/update")
    public ApiResponse<OpsTaskDTO> update(@Valid @RequestBody OpsTaskDTO dto) {
        return ApiResponse.ok(opsTaskService.update(dto));
    }

    /**
     * 逻辑删除任务
     * @param id 任务ID
     * @return 操作结果
     */
    @Operation(summary = "", description = "", operationId = "deleteOpsTask")
    @PostMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        opsTaskService.delete(id);
        return ApiResponse.ok(null);
    }

    /**
     * 获取任务详情
     * @param id 任务ID
     * @return 任务信息
     */
    @Operation(summary = "", description = "", operationId = "getOpsTask")
    @GetMapping("/get/{id}")
    public ApiResponse<OpsTaskDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(opsTaskService.getById(id));
    }

    /**
     * 查询租户下全部有效任务
     * @param tenantId 租户ID
     * @return 任务列表
     */
    @Operation(summary = "", description = "", operationId = "getOpsTasksByTenant")
    @GetMapping("/list")
    public ApiResponse<PageResult<OpsTaskDTO>> listByTenant(@RequestParam Long tenantId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(opsTaskService.listByTenant(tenantId,page,size));
    }

    /**
     * 查询租户下指定状态的任务
     * @param tenantId 租户ID
     * @param status 状态
     * @return 任务列表
     */
    @Operation(summary = "", description = "", operationId = "getOpsTasksByTenantAndStatus")
    @GetMapping("/list-by-status")
    public ApiResponse<PageResult<OpsTaskDTO>> listByTenantAndStatus(@RequestParam Long tenantId, @RequestParam String status,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(opsTaskService.listByTenantAndStatus(tenantId, status,page,size));
    }
}
