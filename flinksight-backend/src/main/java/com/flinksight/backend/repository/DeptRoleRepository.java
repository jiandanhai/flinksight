package com.flinksight.backend.repository;

import com.flinksight.backend.domain.DeptRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeptRoleRepository extends JpaRepository<DeptRole, Long> {
    Page<DeptRole> findByDeptIdAndIsDeleted(Long deptId, Integer isDeleted, Pageable pageable);
    Page<DeptRole> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted, Pageable pageable);

    List<DeptRole> findByDeptIdAndRoleIdAndIsDeleted(Long deptId, Long roleId, Integer isDeleted);
}
