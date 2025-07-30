package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Alert;
import com.flinksight.common.dto.AlertDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface AlertStructMapper extends GenericMapper<AlertDTO, Alert> {}
