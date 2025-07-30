package com.flinksight.backend.repository;

import com.flinksight.backend.domain.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {
    List<RoleMenu> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted);
    List<RoleMenu> findByMenuIdAndIsDeleted(Long menuId, Integer isDeleted);
}
