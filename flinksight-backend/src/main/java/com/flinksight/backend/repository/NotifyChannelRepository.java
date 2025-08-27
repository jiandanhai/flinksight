package com.flinksight.backend.repository;

import com.flinksight.backend.domain.NotifyChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 通知渠道数据仓储层
 */
@Repository
public interface NotifyChannelRepository extends JpaRepository<NotifyChannel, Long> {
    Page<NotifyChannel> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);

    Optional<NotifyChannel> findByIdAndTenantIdAndIsDeleted(Long id, Long tenantId, Integer isDeleted);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update NotifyChannel c set c.isDeleted = 1 " +
            "where c.id = :id and c.tenantId = :tenantId and c.isDeleted = 0")
    int softDeleteByIdAndTenantId(@Param("id") Long id,
                                  @Param("tenantId") Long tenantId);

}
