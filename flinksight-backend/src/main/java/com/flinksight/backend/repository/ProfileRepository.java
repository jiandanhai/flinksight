package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Profile findByTenantIdAndUserIdAndIsDeleted(Long tenantId, Long userId,Integer isDeleted);

    boolean existsByTenantIdAndUserId(Long tenantId, Long userId);

    Page<Profile> findByTenantId(Long tenantId, Pageable pageable);

    @Modifying
    @Query("update Profile p set p.isDeleted=1 where p.tenantId=:tenantId and p.userId=:userId")
    int softDeleteByTenantAndUser(@Param("tenantId") Long tenantId,
                                  @Param("userId") Long userId);
}
