package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Alert;
import com.flinksight.common.dto.AlertDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface AlertStructMapper extends GenericMapper<AlertDTO, Alert> {}
