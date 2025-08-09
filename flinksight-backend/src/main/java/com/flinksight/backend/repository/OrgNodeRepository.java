package com.flinksight.backend.repository;

import com.flinksight.backend.domain.OrgNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrgNodeRepository extends JpaRepository<OrgNode, Long> {
    Page<OrgNode> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);
    // 直接查租户所有未删节点，flat结构
    @Query("SELECT n FROM OrgNode n WHERE n.tenantId = :tenantId AND n.isDeleted = 0")
    List<OrgNode> findAllByTenantId(@Param("tenantId") Long tenantId);
}
