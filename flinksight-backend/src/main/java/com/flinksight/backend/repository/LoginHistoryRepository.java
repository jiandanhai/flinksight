package com.flinksight.backend.repository;

import com.flinksight.backend.domain.LoginHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    Page<LoginHistory> findByUserIdAndIsDeleted(Long userId, Integer isDeleted, Pageable pageable);

    Page<LoginHistory> findByUserIdAndIsDeletedOrderByLoginTimeDesc(Long userId, Integer isDeleted, Pageable pageable);

    Page<LoginHistory> findByTenantIdAndIsDeleted(Long tenantId, Integer isDeleted,Pageable pageable);

    long countByUserIdAndSuccessFlagAndIsDeleted(Long userId, Integer successFlag, Integer isDeleted);
}
