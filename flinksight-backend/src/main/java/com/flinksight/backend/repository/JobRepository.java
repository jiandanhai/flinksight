package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Job;
import com.flinksight.backend.repository.projection.KeyCountView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
    Page<Job> findAllByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<Job> findByIsDeleted(Integer isDeleted, Pageable pageable);
    int countByTenantIdAndIsDeleted(Long tenantId, int isDeleted);
    int countByTenantIdAndStatusAndIsDeleted(Long tenantId, Integer status, int isDeleted);

    @Query("""
      select j.status as key, count(j.id) as cnt
      from Job j
      where j.tenantId = :tenantId and j.isDeleted = :isDeleted
      group by j.status
    """)
    List<KeyCountView> countJobByStatusGroup(@Param("tenantId") Long tenantId, @Param("isDeleted") int isDeleted);
}
