package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    Page<UserGroup> findByUserIdAndIsDeleted(Long userId, Integer isDeleted, Pageable pageable);
    Page<UserGroup> findByGroupIdAndIsDeleted(Long groupId, Integer isDeleted, Pageable pageable);
    List<UserGroup> findByUserIdAndGroupIdAndIsDeleted(Long userId, Long groupId, Integer isDeleted);
}
