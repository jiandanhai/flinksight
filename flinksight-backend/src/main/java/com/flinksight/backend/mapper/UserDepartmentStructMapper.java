package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.UserDepartment;
import com.flinksight.common.dto.UserDepartmentDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface UserDepartmentStructMapper extends GenericMapper<UserDepartmentDTO, UserDepartment> {}
