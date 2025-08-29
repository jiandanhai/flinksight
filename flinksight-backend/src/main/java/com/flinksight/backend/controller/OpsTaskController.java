package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.FunnelResponseDTO;
import com.flinksight.common.dto.KpiResponseDTO;
import com.flinksight.common.dto.OpsTaskDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.OpsTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 运维自动化任务管理控制器
 * 提供运维任务的RESTful API入口
 */
@Tag(name = "api",description = "运维自动化任务管理控制器API")
@RestController
@RequestMapping("/api/ops/task")
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

    @Operation(summary = "", description = "", operationId = "runOpsTask")
    @PostMapping("/run/{id}")
    public ApiResponse<OpsTaskDTO> run(@PathVariable("id") Long id) {
        return ApiResponse.ok(opsTaskService.start(id));
    }

    /**
     * 更新运维任务
     * @param dto 运维任务参数
     * @return 更新后的任务信息
     */
    @Operation(summary = "", description = "", operationId = "updateOpsTask")
    @PutMapping("/update")
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
        opsTaskService.sDelete(id);
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
     * 任务列表
     * status 为必传，但支持传 ALL 表示不过滤（A 方案）
     */
    @Operation(summary = "查询租户下指定状态的任务", description = "", operationId = "listOpsTasks")
    @GetMapping("/list")
    public ApiResponse<PageResult<OpsTaskDTO>> list(
            @RequestParam("status") String status,                          // 必传；ALL 表示不过滤
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "templateType", required = false) String templateType,
            @RequestParam(value = "page", defaultValue = "1") int page,     // 1 基
            @RequestParam(value = "size", defaultValue = "30") int size
    ) {
        return ApiResponse.ok( opsTaskService.list(status, keyword, templateType, page, size));
    }


    @Operation(summary = "KPI 趋势（新建数/成功数）", description = "", operationId = "opsGetKpi")
    @GetMapping("/kpi")
    public ApiResponse<KpiResponseDTO> kpi(@RequestParam(value = "from", required = false)
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                            @RequestParam(value = "to", required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ApiResponse.ok(opsTaskService.getKpi(from, to));
    }

    @Operation(summary = "转化漏斗", description = "", operationId = "opsGetFunnel")
    @GetMapping("/funnel")
    public ApiResponse<FunnelResponseDTO> funnel(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.ok(opsTaskService.getFunnel(date));
    }
}
