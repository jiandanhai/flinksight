package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.OpsTask;
import com.flinksight.common.dto.OpsTaskDTO;
import org.mapstruct.Mapper;

/**
 * 运维自动化任务DTO-实体转换
 * 负责DTO与Entity的双向转换
 */
@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface OpsTaskMapper  extends GenericMapper<OpsTaskDTO, OpsTask> {
}
