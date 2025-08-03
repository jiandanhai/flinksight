package com.flinksight.backend.repository;

import com.flinksight.backend.domain.GroupRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRoleRepository extends JpaRepository<GroupRole, Long> {
    Page<GroupRole> findByGroupIdAndIsDeleted(Long groupId, Integer isDeleted, Pageable pageable);
    Page<GroupRole> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted, Pageable pageable);
    List<GroupRole> findByGroupIdAndRoleIdAndIsDeleted(Long groupId, Long roleId, Integer isDeleted);
}
