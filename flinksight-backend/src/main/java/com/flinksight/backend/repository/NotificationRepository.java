package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndIsDeleted(Long userId, Integer isDeleted);
    List<Notification> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
