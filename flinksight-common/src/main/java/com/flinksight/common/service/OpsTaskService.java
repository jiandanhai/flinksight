package com.flinksight.common.service;

import com.flinksight.common.dto.OpsTaskDTO;
import com.flinksight.common.model.PageResult;

/**
 * 运维自动化任务业务接口
 * 负责任务的增删改查、租户隔离、状态控制等业务逻辑
 */
public interface OpsTaskService extends SoftDeleteService<OpsTaskDTO, Long>{

    /**
     * 新建运维任务
     * @param dto 运维任务参数
     * @return 新建后的运维任务DTO
     */
    OpsTaskDTO create(OpsTaskDTO dto);

    /**
     * 更新运维任务
     * @param dto 运维任务参数
     * @return 更新后的运维任务DTO
     */
    OpsTaskDTO update(OpsTaskDTO dto);

    /**
     * 逻辑删除运维任务
     * @param id 任务ID
     */
    void delete(Long id);

    /**
     * 根据ID获取任务详情
     * @param id 任务ID
     * @return 任务详情DTO
     */
    OpsTaskDTO getById(Long id);

    /**
     * 查询租户下全部有效任务
     * @param tenantId 租户ID
     * @return 运维任务列表
     */
    PageResult<OpsTaskDTO> listByTenant(Long tenantId, int page, int size);

    /**
     * 查询租户下某状态的全部任务
     * @param tenantId 租户ID
     * @param status 任务状态
     * @return 运维任务列表
     */
    PageResult<OpsTaskDTO> listByTenantAndStatus(Long tenantId, String status, int page, int size);

}
