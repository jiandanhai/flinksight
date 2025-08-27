package com.flinksight.common.service;

import com.flinksight.common.dto.OperationTemplateDTO;
import com.flinksight.common.model.PageResult;

import java.util.Map;

public interface OperationTemplateService extends SoftDeleteService<OperationTemplateDTO, Long> {
    OperationTemplateDTO getById(Long id);

    PageResult<OperationTemplateDTO> list(String type, String keyword, int page, int size);

    OperationTemplateDTO create(OperationTemplateDTO dto);

    OperationTemplateDTO update(Long id, OperationTemplateDTO dto);
    /** 渲染（不执行），便于前端预览 */
    String dryRun(Long id, Map<String, Object> vars);
    /** 执行模板（渲染 + 路由执行器） */
    String execute(Long id, Map<String, Object> vars);
}
