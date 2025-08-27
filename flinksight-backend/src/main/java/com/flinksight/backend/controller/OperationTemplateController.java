package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.OperationTemplateDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.OpAudit;
import com.flinksight.common.service.OperationTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 操作模板管理
 */
@Tag(name = "api",description = "操作模板管理API")
@RestController
@RequestMapping("/api/operation-template")
@RequiredArgsConstructor
@Validated
public class OperationTemplateController {

    private final OperationTemplateService service;

    @Operation(summary = "分页查询模板（按 type/keyword 可选过滤）", operationId = "listOperationTemplates")
    @GetMapping("/list")
    public ApiResponse<PageResult<OperationTemplateDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false, name = "keyword") String nameKeyword
    ) {
        return ApiResponse.ok(service.list(type, nameKeyword, page,size));
    }

    @Operation(summary = "模板详情", operationId = "optDetail")
    @GetMapping("/id/{id}")
    public ApiResponse<OperationTemplateDTO> detail(@PathVariable Long id) {
        return ApiResponse.ok(service.getById(id));
    }

    @Operation(summary = "创建模板", operationId = "createOpt")
    @PostMapping("/create")
    @OpAudit(
            action = "OPTPL_CREATE",
            targetType = "OperationTemplate",
            targetIdSpEL = "#ret?.data?.id",
            contentSpEL  = "'name=' + #dto?.name + ',type=' + #dto?.type"
    )
    public ApiResponse<OperationTemplateDTO> create(@RequestBody @Valid OperationTemplateDTO dto) {
        return ApiResponse.ok(service.create(dto));
    }

    @Operation(summary = "更新模板", operationId = "updateOpt")
    @PostMapping("/update/{id}")
    @OpAudit(
            action = "OPTPL_UPDATE",
            targetType = "OperationTemplate",
            targetIdSpEL = "#id",
            contentSpEL  = "'name=' + #dto?.name + ',type=' + #dto?.type"
    )
    public ApiResponse<OperationTemplateDTO> update(@PathVariable Long id, @RequestBody @Valid OperationTemplateDTO dto) {
        return ApiResponse.ok(service.update(id, dto));
    }

    @Operation(summary = "删除模板（软删）", operationId = "deleteOpt")
    @PostMapping("/delete/{id}")
    @OpAudit(
            action = "OPTPL_DELETE",
            targetType = "OperationTemplate",
            targetIdSpEL = "#id"
    )
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return ApiResponse.ok(service.sDelete(id));
    }

    @Operation(summary = "Dry-Run：仅渲染不执行，返回渲染结果便于预览", operationId = "dryRun")
    @PostMapping("/dry-run/{id}")
    @OpAudit(
            action = "OPTPL_DRYRUN",
            targetType = "OperationTemplate",
            targetIdSpEL = "#id",
            contentSpEL  = "'vars=' + #vars"
    )
    public ApiResponse<String> dryRun(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> vars) {
        return ApiResponse.ok(service.dryRun(id, vars));
    }

    @Operation(summary = "执行模板：渲染 + 路由执行器", operationId = "execute")
    @PostMapping("/execute/{id}")
    @OpAudit(
            action = "OPTPL_EXECUTE",
            targetType = "OperationTemplate",
            targetIdSpEL = "#id",
            contentSpEL  = "'vars=' + #vars"
    )
    public ApiResponse<String> execute(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> vars) {
        return ApiResponse.ok(service.execute(id, vars));
    }
}
