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

    List<Notification> findByIdInAndUserIdAndTenantIdAndIsDeleted(Collection<Long> ids, Long userId, Long tenantId, Integer isDeleted);

    Optional<Notification> findByIdAndUserIdAndTenantIdAndIsDeleted(Long id, Long userId, Long tenantId, Integer isDeleted);


    @Query(
            value = """
    SELECT n
    FROM Notification n
    WHERE n.userId = :userId
    AND n.tenantId = :tenantId
    AND n.isDeleted = 0
    AND (:readStatus IS NULL OR n.readStatus = :readStatus)
    AND (:hasCategories = false OR n.category IN :categories)
    """,
                countQuery = """
    SELECT COUNT(n)
    FROM Notification n
    WHERE n.userId = :userId
    AND n.tenantId = :tenantId
    AND n.isDeleted = 0
    AND (:readStatus IS NULL OR n.readStatus = :readStatus)
    AND (:hasCategories = false OR n.category IN :categories)
    """
        )
    Page<Notification> pageByCond(
            @Param("userId") Long userId,
            @Param("tenantId") Long tenantId,
            @Param("readStatus") Integer readStatus,
            @Param("hasCategories") boolean hasCategories,
            @Param("categories") List<String> categories,
            Pageable pageable
    );


    @Modifying
    @Query("update Notification n set n.readStatus=1 " +
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
