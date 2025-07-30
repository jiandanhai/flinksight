package com.flinksight.backend.repository;

import com.flinksight.backend.domain.DeptRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeptRoleRepository extends JpaRepository<DeptRole, Long> {
    List<DeptRole> findByDeptIdAndIsDeleted(Long deptId, Integer isDeleted);
    List<DeptRole> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted);
}
