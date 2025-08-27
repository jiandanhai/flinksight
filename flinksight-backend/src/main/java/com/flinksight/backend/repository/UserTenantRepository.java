package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserTenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserTenantRepository extends JpaRepository<UserTenant, Long> {
    Page<UserTenant> findByUserIdAndIsDeleted(Long userId, Integer isDeleted, Pageable pageable);
    Page<UserTenant> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<UserTenant> findByUserIdAndTenantIdAndIsDeleted(Long userId, Long tenantId, Integer isDeleted, Pageable pageable);

    boolean existsByUserIdAndTenantIdAndIsDeleted(Long userId, Long tenantId, Integer isDeleted);

    @Modifying
    @Transactional
    @Query("update UserTenant ut set ut.isDeleted = 1 where ut.userId = :userId and ut.tenantId = :tenantId and ut.isDeleted = 0")
    int softDeleteByUserIdAndTenantId(Long userId, Long tenantId);

    // 你也可以加真正物理删除的方法
    // void deleteByUserIdAndTenantId(Long userId, Long tenantId);

    @Query("""
    SELECT ut FROM UserTenant ut
     WHERE ut.isDeleted = 0
       AND (:userId   IS NULL OR ut.userId   = :userId)
       AND (:tenantId IS NULL OR ut.tenantId = :tenantId)
    """)
    Page<UserTenant> pageQuery(@Param("userId") Long userId,
                               @Param("tenantId") Long tenantId,
                               Pageable pageable);
}
