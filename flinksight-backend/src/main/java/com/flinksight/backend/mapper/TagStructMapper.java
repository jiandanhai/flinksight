package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Tag;
import com.flinksight.common.dto.TagDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface TagStructMapper extends GenericMapper<TagDTO, Tag> {}
