package com.flinksight.backend.repository;

import com.flinksight.backend.domain.RoleMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Long> {
    Page<RoleMenu> findByRoleIdAndIsDeleted(Long roleId, Integer isDeleted, Pageable pageable);
    Page<RoleMenu> findByMenuIdAndIsDeleted(Long menuId, Integer isDeleted, Pageable pageable);
    List<RoleMenu> findByRoleIdAndMenuIdAndIsDeleted(Long roleId, Long menuId, Integer isDeleted);
}
