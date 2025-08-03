package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    Page<UserPermission> findByUserIdAndIsDeleted(Long userId, Integer isDeleted, Pageable pageable);
    Page<UserPermission> findByPermissionIdAndIsDeleted(Long permissionId, Integer isDeleted, Pageable pageable);
    List<UserPermission> findByUserIdAndPermissionIdAndIsDeleted(Long userId, Long permissionId, Integer isDeleted);
}
