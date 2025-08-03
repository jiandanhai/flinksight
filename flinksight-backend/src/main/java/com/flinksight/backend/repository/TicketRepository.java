package com.flinksight.backend.repository;

import com.flinksight.backend.domain.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 工单表数据访问接口
 * Ticket Repository
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, SoftDeleteRepository<Ticket, Long>  {
    Page<Ticket> findByTenantIdAndStatusAndIsDeleted(Long tenantId, Integer status, Integer isDeleted, Pageable pageable);
}
