package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.DataSource;
import com.flinksight.common.dto.DataSourceDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface DataSourceStructMapper extends GenericMapper<DataSourceDTO, DataSource> {}
