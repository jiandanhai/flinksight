package com.flinksight.common.service;

import com.flinksight.common.dto.TicketDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

/**
 * 工单业务接口
 * Ticket Service
 */
public interface TicketService extends SoftDeleteService<TicketDTO, Long> {
    TicketDTO createTicket(TicketDTO ticket);
    Optional<TicketDTO> getTicketById(Long id);
    PageResult<TicketDTO> getTicketsByTenantAndStatus(Long tenantId, Integer status,int page, int size);
    TicketDTO updateTicket(TicketDTO ticket);
}
