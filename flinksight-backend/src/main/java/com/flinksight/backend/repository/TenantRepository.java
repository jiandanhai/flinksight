package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Role;
import com.flinksight.backend.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 租户表数据访问接口
 * Tenant Repository
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long>, SoftDeleteRepository<Tenant, Long>  {
    Optional<Tenant> findByCode(String code);
}
