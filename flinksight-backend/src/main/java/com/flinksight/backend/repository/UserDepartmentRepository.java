package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserDepartmentRepository extends JpaRepository<UserDepartment, Long> {
    List<UserDepartment> findByUserIdAndIsDeleted(Long userId, Integer isDeleted);
    List<UserDepartment> findByDepartmentIdAndIsDeleted(Long departmentId, Integer isDeleted);
}
