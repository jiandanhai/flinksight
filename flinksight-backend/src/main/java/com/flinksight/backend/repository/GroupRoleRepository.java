package com.flinksight.backend.repository;

import com.flinksight.backend.domain.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupRoleRepository extends JpaRepository<GroupRole, Long> {
    List<GroupRole> findByGroupIdAndIsDeleted(Long groupId, Integer isDeleted);
    List<GroupRole> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted);
}
