package com.flinksight.backend.repository;

import com.flinksight.backend.domain.RoleDataScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoleDataScopeRepository extends JpaRepository<RoleDataScope, Long> {
    List<RoleDataScope> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted);
    List<RoleDataScope> findByDataScopeIdAndIsDeleted(Long dataScopeId, Integer isDeleted);
}
