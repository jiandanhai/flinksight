package com.flinksight.backend.controller;

import com.flinksight.backend.common.ApiResponse;
import com.flinksight.common.dto.OrgNodeDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.OrgNodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "api",description = "组织节点管理API")
@RestController
@RequestMapping("/api/org-node")
@RequiredArgsConstructor
public class OrgNodeController {

    private final OrgNodeService orgNodeService;

    @Operation(summary = "", description = "", operationId = "createOrgNode")
    @PostMapping("/create")
    public ApiResponse<OrgNodeDTO> create(@Valid @RequestBody OrgNodeDTO dto) {
        return ApiResponse.ok(orgNodeService.create(dto));
    }

    @Operation(summary = "", description = "", operationId = "updateOrgNode")
    @PostMapping("/update")
    public ApiResponse<OrgNodeDTO> update(@Valid @RequestBody OrgNodeDTO dto) {
        return ApiResponse.ok(orgNodeService.update(dto));
    }

    @Operation(summary = "", description = "", operationId = "deleteOrgNode")
    @PostMapping("/delete/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        orgNodeService.delete(id);
        return ApiResponse.ok(null);
    }

    @Operation(summary = "", description = "", operationId = "getOrgNode")
    @GetMapping("/get/{id}")
    public ApiResponse<OrgNodeDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(orgNodeService.getById(id));
    }

    @Operation(summary = "", description = "", operationId = "getOrgNodesByTenant")
    @GetMapping("/list")
    public ApiResponse<PageResult<OrgNodeDTO>> list(@RequestParam Long tenantId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(orgNodeService.listByTenant(tenantId,page,size));
    }

    @Operation(summary = "", description = "", operationId = "getOrgNodeTree")
    @GetMapping("/tree")
    public ApiResponse<List<OrgNodeDTO>> tree(@RequestParam Long tenantId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int siz) {
        return ApiResponse.ok(orgNodeService.getOrgTree(tenantId));
    }
}
