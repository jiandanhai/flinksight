package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Job;
import com.flinksight.common.service.projection.KeyCountView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务表数据访问接口
 * Job Repository
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long>, SoftDeleteRepository<Job, Long> {
    Page<Job> findAllByTenantIdAndClusterIdAndIsDeleted(Long tenantId, Long clusterId, Integer isDeleted, Pageable pageable);

    Page<Job> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);

    int countByTenantIdAndIsDeleted(Long tenantId, int isDeleted);

    int countByTenantIdAndStatusAndIsDeleted(Long tenantId, Integer status, int isDeleted);

    @Query("""
      select j.status as key, count(j.id) as cnt
      from Job j
      where j.tenantId = :tenantId and j.isDeleted = :isDeleted
      group by j.status
    """)
    List<KeyCountView> countJobByStatusGroup(@Param("tenantId") Long tenantId, @Param("isDeleted") int isDeleted);

    /**
     * 批量更新状态（仅未软删数据），带租户隔离
     * - JPQL 的 CURRENT_TIMESTAMP 会映射为数据库当前时间
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Job j
           SET j.status    = :status,
               j.updatedAt = CURRENT_TIMESTAMP
         WHERE j.tenantId  = :tenantId
           AND j.isDeleted = 0
           AND j.id IN :ids
        """)
    int batchUpdateStatus(@Param("tenantId") Long tenantId,
                          @Param("ids") java.util.Collection<Long> ids,
                          @Param("status") Integer status);
}
