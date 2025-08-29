package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    Optional<Role> findByTenantIdAndCode(Long tenantId,String code);

    Page<Role> findByTenantIdAndIsDeleted(Long tenantId,Integer isDeleted, Pageable pageable);

    /**
     * 角色名称模糊分页
     */
    Page<Role> findByTenantIdAndNameAndIsDeleted(Long tenantId,String name, Integer isDeleted, Pageable pageable);

    @Query("""
    SELECT r
    FROM Role r
    WHERE r.tenantId = :tenantId
      AND r.isDeleted = 0
      AND ( :keyword IS NULL 
            OR r.name LIKE CONCAT('%', :keyword, '%') 
            OR r.code LIKE CONCAT('%', :keyword, '%') )
      AND ( :name IS NULL OR r.name = :name )
      AND ( :code IS NULL OR r.code = :code )
""")
    Page<Role> searchByTenantAndFilters(
            @Param("tenantId") Long tenantId,
            @Param("keyword") String keyword,
            @Param("name") String name,
            @Param("code") String code,
            Pageable pageable
    );

    /**
     * 唯一校验
     */
    boolean existsByTenantIdAndNameAndIsDeleted(Long tenantId,String name, Integer isDeleted);
}
