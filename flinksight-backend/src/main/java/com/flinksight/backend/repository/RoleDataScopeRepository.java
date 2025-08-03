package com.flinksight.backend.repository;

import com.flinksight.backend.domain.RoleDataScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDataScopeRepository extends JpaRepository<RoleDataScope, Long> {
    Page<RoleDataScope> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted, Pageable pageable);
    Page<RoleDataScope> findByDataScopeIdAndIsDeleted(Long dataScopeId, Integer isDeleted, Pageable pageable);
    List<RoleDataScope> findByRoleIdAndDataScopeIdAndIsDeleted(Long roleId, Long dataScopeId, Integer isDeleted);
}
