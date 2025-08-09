package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
    Optional<Role> findByCode(String code);

    Page<Role> findByIsDeleted(Integer isDeleted, Pageable pageable);

    /**
     * 角色名称模糊分页
     */
    Page<Role> findByNameAndIsDeleted(String name, Integer isDeleted, Pageable pageable);

    /**
     * 唯一校验
     */
    boolean existsByNameAndIsDeleted(String name, Integer isDeleted);
}
