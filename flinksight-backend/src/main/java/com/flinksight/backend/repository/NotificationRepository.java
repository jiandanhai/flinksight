package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserIdAndIsDeleted(Long userId, Integer isDeleted, Pageable pageable);
    Page<Notification> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<Notification> findByIsDeleted(Integer isDeleted, Pageable pageable);
}
