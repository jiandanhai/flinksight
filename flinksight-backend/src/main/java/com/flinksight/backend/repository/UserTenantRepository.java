package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserTenantRepository extends JpaRepository<UserTenant, Long> {
    List<UserTenant> findByUserIdAndIsDeleted(Long userId, Integer isDeleted);
    List<UserTenant> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted);
}
