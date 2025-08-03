package com.flinksight.backend.service;

import com.flinksight.backend.domain.Ticket;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.TicketStructMapper;
import com.flinksight.backend.repository.TicketRepository;
import com.flinksight.backend.security.tenant.TenantRequired;
import com.flinksight.common.dto.TicketDTO;
import com.flinksight.common.enums.ErrorCode;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.TicketService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    private final TicketStructMapper ticketStructMapper;

    @Override
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        Ticket entity = ticketStructMapper.toEntity(ticketDTO);
        entity.setIsDeleted(0);
        Ticket saved = repository.save(entity);
        return ticketStructMapper.toDTO(saved);
    }

    @Override
    public Optional<TicketDTO> getTicketById(Long id) {
        return repository.findById(id).map(ticketStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<TicketDTO> getTicketsByTenantAndStatus(Long tenantId, Integer status,int page, int size) {
        Page<Ticket> result = repository.findByTenantIdAndStatusAndIsDeleted(tenantId, status,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<TicketDTO> dtoPage = result.map(ticketStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public TicketDTO updateTicket(TicketDTO ticketDTO) {
        Optional<TicketDTO> opt = repository.findById(ticketDTO.getId()).map(ticketStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        Ticket entity = ticketStructMapper.toEntity(ticketDTO);
        if(opt.isPresent()) {
            TicketDTO t = opt.get();
            entity.setStatus(t.getStatus());
            entity.setHandlerId(t.getHandlerId());
            entity.setNote(t.getNote());
            entity.setUpdateTime(t.getUpdateTime());
            entity.setIsDeleted(0);
            // 其它业务字段
            return ticketStructMapper.toDTO(repository.save(entity));
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "工单不存在");
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<TicketDTO> opt = repository.findById(id).map(ticketStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            TicketDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(ticketStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
