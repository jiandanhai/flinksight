package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.AuditLog;
import com.flinksight.common.dto.AuditLogDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface AuditLogStructMapper extends GenericMapper<AuditLogDTO, AuditLog> {}
