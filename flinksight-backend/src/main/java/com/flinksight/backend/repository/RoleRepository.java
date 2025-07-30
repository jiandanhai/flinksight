package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Permission;
import com.flinksight.backend.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 角色表数据访问接口
 * Role Repository
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, SoftDeleteRepository<Role, Long>  {

    /**
     * 根据角色编码查询
     * @param code 角色编码
     * @return 角色对象
     */
    Role findByCode(String code);
}
