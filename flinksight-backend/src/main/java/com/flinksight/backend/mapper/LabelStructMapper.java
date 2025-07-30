package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Label;
import com.flinksight.common.dto.LabelDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface LabelStructMapper extends GenericMapper<LabelDTO, Label> {}
