// BaseStructMapperConfig.java
package com.flinksight.backend.mapper;

import org.mapstruct.MapperConfig;

/**
 * MapStruct基础配置，所有Mapper可继承本配置（约定componentModel和其它全局配置）
 */
@MapperConfig(componentModel = "spring")
public interface BaseStructMapperConfig {
}
