package com.flinksight.backend.repository;

import com.flinksight.backend.domain.OpsTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 运维自动化任务数据仓储层
 * 提供运维任务的CRUD及按租户/状态等自定义查询
 */
@Repository
public interface OpsTaskRepository extends JpaRepository<OpsTask, Long> {

    /**
     * 按租户ID和软删标志查询全部任务
     * @param tenantId 租户ID
     * @param isDeleted 软删除标志 0=正常 1=删除
     * @return 运维任务列表
     */
    Page<OpsTask> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);

    /**
     * 按租户ID、任务状态查询
     * @param tenantId 租户ID
     * @param status 状态
     * @param isDeleted 软删除标志
     * @return 运维任务列表
     */
    Page<OpsTask> findByTenantIdAndStatusAndIsDeleted(Long tenantId, String status, Integer isDeleted, Pageable pageable);

}
