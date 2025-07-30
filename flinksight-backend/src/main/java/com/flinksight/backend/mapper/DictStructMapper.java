package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.Dict;
import com.flinksight.common.dto.DictDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface DictStructMapper extends GenericMapper<DictDTO, Dict> {}
