package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.DeptRole;
import com.flinksight.common.dto.DeptRoleDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface DeptRoleStructMapper extends GenericMapper<DeptRoleDTO, DeptRole> {}
