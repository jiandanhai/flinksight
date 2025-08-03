// JobStructMapper.java
package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Job;
import com.flinksight.common.dto.JobDTO;
import org.mapstruct.Mapper;

@Mapper(config = BaseStructMapperConfig.class, componentModel = "spring")
public interface JobStructMapper extends GenericMapper<JobDTO, Job> {
    // 特殊字段需要映射时补充 @Mapping 注解
}
