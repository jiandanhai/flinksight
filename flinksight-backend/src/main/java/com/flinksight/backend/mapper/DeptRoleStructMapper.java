package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.DeptRole;
import com.flinksight.common.dto.DeptRoleDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface DeptRoleStructMapper extends GenericMapper<DeptRoleDTO, DeptRole> {}
