package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDepartmentRepository extends JpaRepository<UserDepartment, Long> {
    Page<UserDepartment> findByUserIdAndIsDeleted(Long userId, Integer isDeleted, Pageable pageable);
    Page<UserDepartment> findByDepartmentIdAndIsDeleted(Long departmentId, Integer isDeleted, Pageable pageable);

    List<UserDepartment> findByUserIdAndDepartmentIdAndIsDeleted(Long userId, Long departmentId, Integer isDeleted);

    @Query("""
    SELECT ud FROM UserDepartment ud
     WHERE ud.isDeleted = 0
       AND (:userId       IS NULL OR ud.userId       = :userId)
       AND (:departmentId IS NULL OR ud.departmentId = :departmentId)
  """)
    Page<UserDepartment> pageQuery(@Param("userId") Long userId,
                                   @Param("departmentId") Long departmentId,
                                   Pageable pageable);
}
