package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByTenantIdAndUserIdAndIsDeleted(Long tenantId,Long userId, Integer isDeleted, Pageable pageable);
    Page<Notification> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    Page<Notification> findByIsDeleted(Integer isDeleted, Pageable pageable);

    Page<Notification> findByUserIdAndTenantIdAndIsDeleted(Long userId, Long tenantId, Integer isDeleted, Pageable pageable);

    Page<Notification> findByUserIdAndTenantIdAndIsDeletedAndType(Long userId, Long tenantId, Integer isDeleted, String type, Pageable pageable);

    Page<Notification> findByUserIdAndTenantIdAndIsDeletedAndIsRead(Long userId, Long tenantId, Integer isDeleted, Integer isRead, Pageable pageable);

    List<Notification> findByIdInAndUserIdAndTenantIdAndIsDeleted(Collection<Long> ids, Long userId, Long tenantId, Integer isDeleted);

    Optional<Notification> findByIdAndUserIdAndTenantIdAndIsDeleted(Long id, Long userId, Long tenantId, Integer isDeleted);


    @Modifying
    @Query("update Notification n set n.isRead=1 " +
            "where n.id=:id and n.userId=:uid and n.tenantId=:tid and n.isDeleted=0")
    int markReadByIdAndOwner(@Param("id") Long id,
                             @Param("uid") Long uid,
                             @Param("tid") Long tid);

    @Modifying
    @Query("update Notification n set n.isDeleted=1 " +
            "where n.id in :ids and n.userId=:uid and n.tenantId=:tid and n.isDeleted=0")
    int softDeleteByIdsAndOwner(@Param("ids") Collection<Long> ids,
                                @Param("uid") Long uid,
                                @Param("tid") Long tid);

    @Modifying
    @Query("update Notification n set n.isDeleted=1 " +
            "where n.id = :id and n.userId=:uid and n.tenantId=:tid and n.isDeleted=0")
    int softDeleteByIdAndOwner(@Param("id") Long id,
                                @Param("uid") Long uid,
                                @Param("tid") Long tid);
}
