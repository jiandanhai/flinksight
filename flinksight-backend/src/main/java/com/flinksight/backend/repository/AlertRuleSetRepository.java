package com.flinksight.backend.repository;

import com.flinksight.backend.domain.AlertRuleSet;
import com.flinksight.common.enums.RuleSetStatus;
import com.flinksight.common.enums.ScopeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlertRuleSetRepository extends JpaRepository<AlertRuleSet, Long> {

    Optional<AlertRuleSet> findFirstByTenantIdAndScopeTypeAndScopeIdAndActiveFlag(
            Long tenantId, ScopeType scopeType, Long scopeId, Integer activeFlag);

    @Query("select s from AlertRuleSet s where s.tenantId=:tenant and s.scopeType=:type and ((:sid is null and s.scopeId is null) or s.scopeId=:sid) and s.status=:st order by s.version desc")
    List<AlertRuleSet> findByScopeAndStatusDesc(@Param("tenant") Long tenantId,
                                                @Param("type") ScopeType scopeType,
                                                @Param("sid") Long scopeId,
                                                @Param("st") RuleSetStatus status);

    @Modifying
    @Query("update AlertRuleSet s set s.activeFlag=0, s.status='ARCHIVED' where s.tenantId=:tenant and s.scopeType=:type and ((:sid is null and s.scopeId is null) or s.scopeId=:sid) and s.activeFlag=1")
    int deactivateActive(@Param("tenant") Long tenantId, @Param("type") ScopeType scopeType, @Param("sid") Long scopeId);

    boolean existsByTenantIdAndScopeTypeAndScopeIdAndVersion(Long tenantId, ScopeType scopeType, Long scopeId, Long version);

    // 分页检索（可选过滤）：租户、作用域类型、作用域ID、状态、是否激活
    @Query("""
      select s from AlertRuleSet s
       where (:tenantId is null or s.tenantId = :tenantId)
         and (:scopeType is null or s.scopeType = :scopeType)
         and ( (:scopeId is null and s.scopeId is null) or (:scopeId is not null and s.scopeId = :scopeId) )
         and (:status is null or s.status = :status)
         and (:activeFlag is null or s.activeFlag = :activeFlag)
       order by s.version desc
      """)
    Page<AlertRuleSet> search(
            @Param("tenantId") Long tenantId,
            @Param("scopeType") ScopeType scopeType,
            @Param("scopeId") Long scopeId,
            @Param("status") RuleSetStatus status,
            @Param("activeFlag") Integer activeFlag,
            Pageable pageable
    );
}
