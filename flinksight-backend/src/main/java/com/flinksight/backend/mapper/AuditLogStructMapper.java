package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.backend.domain.AuditLog;
import com.flinksight.common.dto.AuditLogDTO;

@Mapper(config = BaseStructMapperConfig.class)
public interface AuditLogStructMapper extends GenericMapper<AuditLogDTO, AuditLog> {}
