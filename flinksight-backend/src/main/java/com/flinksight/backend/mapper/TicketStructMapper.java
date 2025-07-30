package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Ticket;
import com.flinksight.common.dto.TicketDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface TicketStructMapper extends GenericMapper<TicketDTO, Ticket> {}
