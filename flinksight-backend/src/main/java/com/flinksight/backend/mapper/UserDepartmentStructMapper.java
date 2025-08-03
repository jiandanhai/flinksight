package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserDepartment;
import com.flinksight.common.dto.UserDepartmentDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface UserDepartmentStructMapper extends GenericMapper<UserDepartmentDTO, UserDepartment> {}
