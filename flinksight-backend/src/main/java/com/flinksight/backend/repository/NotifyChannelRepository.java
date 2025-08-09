package com.flinksight.backend.repository;

import com.flinksight.backend.domain.NotifyChannel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 通知渠道数据仓储层
 */
@Repository
public interface NotifyChannelRepository extends JpaRepository<NotifyChannel, Long> {
    Page<NotifyChannel> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
}
