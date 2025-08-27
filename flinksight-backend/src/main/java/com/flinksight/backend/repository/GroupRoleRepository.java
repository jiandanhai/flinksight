package com.flinksight.backend.repository;

import com.flinksight.backend.domain.GroupRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRoleRepository extends JpaRepository<GroupRole, Long> {

    List<GroupRole> findByGroupIdAndRoleIdAndIsDeleted(Long groupId, Long roleId, Integer isDeleted);

    @Query("""
     SELECT gr FROM GroupRole gr
      WHERE gr.isDeleted = 0
        AND (:groupId IS NULL OR gr.groupId = :groupId)
        AND (:roleId  IS NULL OR gr.roleId  = :roleId)
  """)
    Page<GroupRole> pageQuery(@Param("groupId") Long groupId,
                              @Param("roleId")  Long roleId,
                              Pageable pageable);

    // 可选：用于业务唯一性校验或快速查单条
    boolean existsByGroupIdAndRoleIdAndIsDeleted(Long groupId, Long roleId, Integer isDeleted);
}
