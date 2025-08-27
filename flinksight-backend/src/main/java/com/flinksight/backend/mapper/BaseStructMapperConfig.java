// BaseStructMapperConfig.java
package com.flinksight.backend.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct基础配置，所有Mapper可继承本配置（约定componentModel和其它全局配置）
 */
@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        // 避免 NPE，生成的代码会对源字段做 null 判断
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        // 便于子 Mapper 继承配置
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG
)
public interface BaseStructMapperConfig {}
