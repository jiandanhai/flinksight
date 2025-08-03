package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserTenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTenantRepository extends JpaRepository<UserTenant, Long> {
    Page<UserTenant> findByUserIdAndIsDeleted(Long userId, Integer isDeleted, Pageable pageable);
    Page<UserTenant> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    List<UserTenant> findByUserIdAndTenantIdAndIsDeleted(Long userId, Long tenantId, Integer isDeleted);
}
