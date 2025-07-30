package com.flinksight.backend.service;

import com.flinksight.backend.domain.Tenant;
import com.flinksight.backend.domain.Ticket;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.TenantStructMapper;
import com.flinksight.backend.mapper.TicketStructMapper;
import com.flinksight.backend.repository.TicketRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.TenantDTO;
import com.flinksight.common.dto.TicketDTO;
import com.flinksight.common.service.TicketService;
import com.flinksight.common.enums.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 工单业务实现
 * Ticket Service Impl
 */
@Service
@RequiredArgsConstructor
@Transactional
@TenantRequired
public class TicketServiceImpl implements TicketService{

    private final TicketRepository repository;
    private final TicketStructMapper mapper;

    @Override
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        Ticket entity = mapper.toEntity(ticketDTO);
        entity.setIsDeleted(0);
        Ticket saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<TicketDTO> getTicketById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<TicketDTO> getTicketsByTenantAndStatus(Long tenantId, Integer status) {
        return mapper.toDTOList(repository.findByTenantIdAndStatusAndIsDeleted(tenantId, status, 0));
    }

    @Override
    public TicketDTO updateTicket(TicketDTO ticketDTO) {
        Optional<TicketDTO> opt = repository.findById(ticketDTO.getId()).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        Ticket entity = mapper.toEntity(ticketDTO);
        if(opt.isPresent()) {
            TicketDTO t = opt.get();
            entity.setStatus(t.getStatus());
            entity.setHandlerId(t.getHandlerId());
            entity.setNote(t.getNote());
            entity.setUpdateTime(t.getUpdateTime());
            entity.setIsDeleted(0);
            // 其它业务字段
            return mapper.toDTO(repository.save(entity));
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "工单不存在");
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<TicketDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            TicketDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
