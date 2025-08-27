package com.flinksight.backend.repository;

import com.flinksight.backend.domain.OpsTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 运维自动化任务数据仓储层
 * 提供运维任务的CRUD及按租户/状态等自定义查询
 */
@Repository
public interface OpsTaskRepository extends JpaRepository<OpsTask, Long> {

    Optional<OpsTask> findByIdAndTenantIdAndIsDeleted(Long id, Long tenantId, Integer isDeleted);

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


    /** 统计：按 created_at 的日期分组，计算每日创建任务数（活跃） */
    @Query("""
        select FUNCTION('date', t.createdAt) as d, count(t) 
        from OpsTask t 
        where t.isDeleted = 0 
          and t.tenantId = :tenantId 
          and t.createdAt between :from and :to
        group by FUNCTION('date', t.createdAt)
        order by d asc
        """)
    List<Object[]> countCreatedByDay(Long tenantId, LocalDateTime from, LocalDateTime to);

    /** 统计：按 executed_at（为空则回退到 created_at）分组，且 status=SUCCESS 的每日成功数（付费） */
    @Query("""
        select FUNCTION('date', coalesce(t.executedAt, t.createdAt)) as d, count(t) 
        from OpsTask t 
        where t.isDeleted = 0 
          and t.tenantId = :tenantId 
          and t.status = 'SUCCESS'
          and coalesce(t.executedAt, t.createdAt) between :from and :to
        group by FUNCTION('date', coalesce(t.executedAt, t.createdAt))
        order by d asc
        """)
    List<Object[]> countSuccessByDay(Long tenantId, LocalDateTime from, LocalDateTime to);

    /** 漏斗：给定 bizDate（按 created_at 的日期），各状态计数 */
    @Query("""
        select t.status, count(t) 
        from OpsTask t
        where t.isDeleted = 0
          and t.tenantId = :tenantId
          and FUNCTION('date', t.createdAt) = :bizDate
        group by t.status
        """)
    List<Object[]> countStatusOnDate(Long tenantId, LocalDate bizDate);

    /** 获取有数据的最新业务日期（按 created_at 的 date） */
    @Query("""
        select max(FUNCTION('date', t.createdAt)) 
        from OpsTask t 
        where t.isDeleted = 0 and t.tenantId = :tenantId
        """)
    Optional<LocalDate> findLatestBizDate(Long tenantId);

}
