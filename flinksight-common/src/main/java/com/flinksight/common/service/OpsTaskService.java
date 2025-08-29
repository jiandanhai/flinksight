package com.flinksight.common.service;

import com.flinksight.common.dto.FunnelResponseDTO;
import com.flinksight.common.dto.KpiResponseDTO;
import com.flinksight.common.dto.OpsTaskDTO;
import com.flinksight.common.model.PageResult;

import java.time.LocalDate;

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
     * 根据ID获取任务详情
     * @param id 任务ID
     * @return 任务详情DTO
     */
    OpsTaskDTO getById(Long id);

    PageResult<OpsTaskDTO> list(String status, String keyword, String templateType, int page, int size);

    KpiResponseDTO getKpi(LocalDate from, LocalDate to);

    FunnelResponseDTO getFunnel( LocalDate dateOrNull);

    OpsTaskDTO start(Long id);

    // RUNNING
    OpsTaskDTO complete(OpsTaskDTO req);     // SUCCESS

    OpsTaskDTO fail(OpsTaskDTO req);         // FAILED

    OpsTaskDTO cancel(OpsTaskDTO req);       // CANCELLED

}
