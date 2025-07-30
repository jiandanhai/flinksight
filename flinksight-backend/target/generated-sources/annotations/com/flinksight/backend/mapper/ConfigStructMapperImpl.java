package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Config;
import com.flinksight.common.dto.ConfigDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T19:25:53+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Oracle Corporation)"
)
@Component
public class ConfigStructMapperImpl implements ConfigStructMapper {

    @Override
    public ConfigDTO toDTO(Config entity) {
        if ( entity == null ) {
            return null;
        }

        ConfigDTO.ConfigDTOBuilder configDTO = ConfigDTO.builder();

        configDTO.id( entity.getId() );
        configDTO.code( entity.getCode() );
        configDTO.value( entity.getValue() );
        configDTO.description( entity.getDescription() );
        configDTO.isDeleted( entity.getIsDeleted() );

        return configDTO.build();
    }

    @Override
    public Config toEntity(ConfigDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Config.ConfigBuilder config = Config.builder();

        config.id( dto.getId() );
        config.code( dto.getCode() );
        config.value( dto.getValue() );
        config.description( dto.getDescription() );
        config.isDeleted( dto.getIsDeleted() );

        return config.build();
    }

    @Override
    public List<ConfigDTO> toDTOList(List<Config> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ConfigDTO> list = new ArrayList<ConfigDTO>( entityList.size() );
        for ( Config config : entityList ) {
            list.add( toDTO( config ) );
        }

        return list;
    }

    @Override
    public List<Config> toEntityList(List<ConfigDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Config> list = new ArrayList<Config>( dtoList.size() );
        for ( ConfigDTO configDTO : dtoList ) {
            list.add( toEntity( configDTO ) );
        }

        return list;
    }
}
