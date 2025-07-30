package com.flinksight.backend.repository;

import com.flinksight.backend.domain.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost, Long> {
    List<UserPost> findByUserIdAndIsDeleted(Long userId, Integer isDeleted);
    List<UserPost> findByPostIdAndIsDeleted(Long postId, Integer isDeleted);
    // 根据角色ID查找所有用户ID
    @Query("select ur.userId from UserRole ur where ur.roleId = :roleId and ur.isDeleted = 0")
    List<Long> findUserIdsByRoleId(@Param("roleId") Long roleId);

    // 根据用户ID查找所有角色ID
    @Query("select ur.roleId from UserRole ur where ur.userId = :userId and ur.isDeleted = 0")
    List<Long> findRoleIdsByUserId(@Param("userId") Long userId);
}
