package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Label;
import com.flinksight.common.dto.LabelDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface LabelStructMapper extends GenericMapper<LabelDTO, Label> {}
