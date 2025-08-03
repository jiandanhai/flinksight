package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Dict;
import com.flinksight.common.dto.DictDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface DictStructMapper extends GenericMapper<DictDTO, Dict> {}
