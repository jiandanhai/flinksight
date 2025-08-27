package com.flinksight.backend.repository;

import com.flinksight.backend.domain.DeptRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeptRoleRepository extends JpaRepository<DeptRole, Long> {

    List<DeptRole> findByDeptIdAndRoleIdAndIsDeleted(Long deptId, Long roleId, Integer isDeleted);

    @Query("""
    SELECT dr FROM DeptRole dr
    WHERE dr.isDeleted = 0
      AND (:deptId IS NULL OR dr.deptId = :deptId)
      AND (:roleId IS NULL OR dr.roleId = :roleId)
  """)
    Page<DeptRole> pageQuery(@Param("deptId") Long deptId,
                        @Param("roleId") Long roleId,
                        Pageable pageable);
}
