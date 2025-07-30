// JobStructMapper.java
package com.flinksight.backend.mapper;

import org.mapstruct.Mapper;
import com.flinksight.common.dto.JobDTO;
import com.flinksight.backend.domain.Job;

@Mapper(config = BaseStructMapperConfig.class)
public interface JobStructMapper extends GenericMapper<JobDTO, Job> {
    // 特殊字段需要映射时补充 @Mapping 注解
}
