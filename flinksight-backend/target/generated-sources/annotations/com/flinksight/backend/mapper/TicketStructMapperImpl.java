package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Ticket;
import com.flinksight.common.dto.TicketDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T19:25:53+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Oracle Corporation)"
)
@Component
public class TicketStructMapperImpl implements TicketStructMapper {

    @Override
    public TicketDTO toDTO(Ticket entity) {
        if ( entity == null ) {
            return null;
        }

        TicketDTO.TicketDTOBuilder ticketDTO = TicketDTO.builder();

        ticketDTO.id( entity.getId() );
        ticketDTO.tenantId( entity.getTenantId() );
        ticketDTO.alertId( entity.getAlertId() );
        ticketDTO.handlerId( entity.getHandlerId() );
        ticketDTO.status( entity.getStatus() );
        ticketDTO.note( entity.getNote() );
        ticketDTO.isDeleted( entity.getIsDeleted() );
        ticketDTO.updateTime( entity.getUpdateTime() );

        return ticketDTO.build();
    }

    @Override
    public Ticket toEntity(TicketDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Ticket.TicketBuilder ticket = Ticket.builder();

        ticket.id( dto.getId() );
        ticket.tenantId( dto.getTenantId() );
        ticket.alertId( dto.getAlertId() );
        ticket.handlerId( dto.getHandlerId() );
        ticket.status( dto.getStatus() );
        ticket.note( dto.getNote() );
        ticket.isDeleted( dto.getIsDeleted() );
        ticket.updateTime( dto.getUpdateTime() );

        return ticket.build();
    }

    @Override
    public List<TicketDTO> toDTOList(List<Ticket> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TicketDTO> list = new ArrayList<TicketDTO>( entityList.size() );
        for ( Ticket ticket : entityList ) {
            list.add( toDTO( ticket ) );
        }

        return list;
    }

    @Override
    public List<Ticket> toEntityList(List<TicketDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Ticket> list = new ArrayList<Ticket>( dtoList.size() );
        for ( TicketDTO ticketDTO : dtoList ) {
            list.add( toEntity( ticketDTO ) );
        }

        return list;
    }
}
