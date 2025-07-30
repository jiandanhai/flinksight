package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    List<UserGroup> findByUserIdAndIsDeleted(Long userId, Integer isDeleted);
    List<UserGroup> findByGroupIdAndIsDeleted(Long groupId, Integer isDeleted);
}
