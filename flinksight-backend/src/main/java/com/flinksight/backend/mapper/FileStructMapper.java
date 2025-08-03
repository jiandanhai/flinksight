package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.File;
import com.flinksight.common.dto.FileDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface FileStructMapper extends GenericMapper<FileDTO, File> {}
