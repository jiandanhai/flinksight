package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    List<UserPermission> findByUserIdAndIsDeleted(Long userId, Integer isDeleted);
    List<UserPermission> findByPermissionIdAndIsDeleted(Long permissionId, Integer isDeleted);
}
