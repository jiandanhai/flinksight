package com.flinksight.backend.repository;

import com.flinksight.backend.domain.RoleDataScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDataScopeRepository extends JpaRepository<RoleDataScope, Long> {
    List<RoleDataScope> findByRoleIdAndDataScopeIdAndIsDeleted(Long roleId, Long dataScopeId, Integer isDeleted);


    @Query("""
    SELECT rds FROM RoleDataScope rds
     WHERE rds.isDeleted = 0
       AND (:roleId      IS NULL OR rds.roleId      = :roleId)
       AND (:dataScopeId IS NULL OR rds.dataScopeId = :dataScopeId)
  """)
    Page<RoleDataScope> pageQuery(@Param("roleId") Long roleId,
                                  @Param("dataScopeId") Long dataScopeId,
                                  Pageable pageable);
}
