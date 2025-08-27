package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    Page<UserGroup> findByUserIdAndIsDeleted(Long userId, Integer isDeleted, Pageable pageable);
    Page<UserGroup> findByGroupIdAndIsDeleted(Long groupId, Integer isDeleted, Pageable pageable);
    List<UserGroup> findByUserIdAndGroupIdAndIsDeleted(Long userId, Long groupId, Integer isDeleted);

    @Query("""
    SELECT ug FROM UserGroup ug
     WHERE ug.isDeleted = 0
       AND (:userId  IS NULL OR ug.userId  = :userId)
       AND (:groupId IS NULL OR ug.groupId = :groupId)
  """)
    Page<UserGroup> pageQuery(@Param("userId") Long userId,
                              @Param("groupId") Long groupId,
                              Pageable pageable);
}
