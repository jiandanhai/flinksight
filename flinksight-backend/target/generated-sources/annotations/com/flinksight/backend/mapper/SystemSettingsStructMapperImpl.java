package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.SystemSettings;
import com.flinksight.common.dto.SystemSettingsDTO;
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
public class SystemSettingsStructMapperImpl implements SystemSettingsStructMapper {

    @Override
    public SystemSettingsDTO toDTO(SystemSettings entity) {
        if ( entity == null ) {
            return null;
        }

        SystemSettingsDTO.SystemSettingsDTOBuilder systemSettingsDTO = SystemSettingsDTO.builder();

        systemSettingsDTO.id( entity.getId() );
        systemSettingsDTO.code( entity.getCode() );
        systemSettingsDTO.value( entity.getValue() );
        systemSettingsDTO.description( entity.getDescription() );
        systemSettingsDTO.isDeleted( entity.getIsDeleted() );

        return systemSettingsDTO.build();
    }

    @Override
    public SystemSettings toEntity(SystemSettingsDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SystemSettings.SystemSettingsBuilder systemSettings = SystemSettings.builder();

        systemSettings.id( dto.getId() );
        systemSettings.code( dto.getCode() );
        systemSettings.value( dto.getValue() );
        systemSettings.description( dto.getDescription() );
        systemSettings.isDeleted( dto.getIsDeleted() );

        return systemSettings.build();
    }

    @Override
    public List<SystemSettingsDTO> toDTOList(List<SystemSettings> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<SystemSettingsDTO> list = new ArrayList<SystemSettingsDTO>( entityList.size() );
        for ( SystemSettings systemSettings : entityList ) {
            list.add( toDTO( systemSettings ) );
        }

        return list;
    }

    @Override
    public List<SystemSettings> toEntityList(List<SystemSettingsDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<SystemSettings> list = new ArrayList<SystemSettings>( dtoList.size() );
        for ( SystemSettingsDTO systemSettingsDTO : dtoList ) {
            list.add( toEntity( systemSettingsDTO ) );
        }

        return list;
    }
}
