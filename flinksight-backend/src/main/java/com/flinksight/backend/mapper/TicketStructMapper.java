package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Ticket;
import com.flinksight.common.dto.TicketDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface TicketStructMapper extends GenericMapper<TicketDTO, Ticket> {}
