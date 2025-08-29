package com.flinksight.backend.repository;

import com.flinksight.backend.domain.OperationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperationTemplateRepository extends JpaRepository<OperationTemplate, Long> {
    Optional<OperationTemplate> findByIdAndTenantId(Long id, Long tenantId);

    Page<OperationTemplate> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted, Pageable pageable);


    Page<OperationTemplate> findByTenantIdAndIsDeletedAndTypeContainingAndNameContaining(
            Long tenantId, Integer isDeleted, String type, String name, Pageable pageable);

    @Query("""
        SELECT o
        FROM OperationTemplate o
        WHERE o.tenantId = :tenantId
          AND o.isDeleted = 0
          AND ( :type IS NULL
                OR LOWER(o.type) LIKE CONCAT('%', LOWER(:type), '%') )
          AND ( :keyword IS NULL
                OR LOWER(o.name) LIKE CONCAT('%', LOWER(:keyword), '%')
                OR LOWER(o.type) LIKE CONCAT('%', LOWER(:keyword), '%')
                OR LOWER(o.content) LIKE CONCAT('%', LOWER(:keyword), '%') 
              )
    """)
    Page<OperationTemplate> searchByTenantTypeKeyword(
            @Param("tenantId") Long tenantId,
            @Param("type") String type,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    Optional<OperationTemplate> findByTenantIdAndTypeAndNameAndIsDeleted(Long tenantId, String type, String name, Integer isDeleted);

    boolean existsByTenantIdAndTypeAndNameAndIsDeleted(Long tenantId, String type, String name, Integer isDeleted);

    /** 更新时做唯一性校验（排除自己） */
    Optional<OperationTemplate> findByTenantIdAndTypeAndNameAndIsDeletedAndIdNot(
            Long tenantId, String type, String name, Integer isDeleted, Long id);


}
