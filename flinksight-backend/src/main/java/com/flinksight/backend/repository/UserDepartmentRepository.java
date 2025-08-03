package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDepartmentRepository extends JpaRepository<UserDepartment, Long> {
    Page<UserDepartment> findByUserIdAndIsDeleted(Long userId, Integer isDeleted, Pageable pageable);
    Page<UserDepartment> findByDepartmentIdAndIsDeleted(Long departmentId, Integer isDeleted, Pageable pageable);

    List<UserDepartment> findByUserIdAndDepartmentIdAndIsDeleted(Long userId, Long departmentId, Integer isDeleted);
}
