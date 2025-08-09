package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Ticket;
import com.flinksight.common.dto.TenantDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface TenantMapper  extends GenericMapper<TenantDTO, Ticket> {}
