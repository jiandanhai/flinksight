package com.flinksight.common.service;

import com.flinksight.common.dto.TicketDTO;

import java.util.List;
import java.util.Optional;

/**
 * 工单业务接口
 * Ticket Service
 */
public interface TicketService extends SoftDeleteService<TicketDTO, Long> {
    TicketDTO createTicket(TicketDTO ticket);
    Optional<TicketDTO> getTicketById(Long id);
    List<TicketDTO> getTicketsByTenantAndStatus(Long tenantId, Integer status);
    TicketDTO updateTicket(TicketDTO ticket);
}
