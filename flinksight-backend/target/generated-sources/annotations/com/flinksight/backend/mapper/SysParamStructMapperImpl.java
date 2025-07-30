package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.SysParam;
import com.flinksight.common.dto.SysParamDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T19:25:54+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Oracle Corporation)"
)
@Component
public class SysParamStructMapperImpl implements SysParamStructMapper {

    @Override
    public SysParamDTO toDTO(SysParam entity) {
        if ( entity == null ) {
            return null;
        }

        SysParamDTO.SysParamDTOBuilder sysParamDTO = SysParamDTO.builder();

        sysParamDTO.id( entity.getId() );
        sysParamDTO.description( entity.getDescription() );
        sysParamDTO.isDeleted( entity.getIsDeleted() );
        sysParamDTO.updateTime( entity.getUpdateTime() );

        return sysParamDTO.build();
    }

    @Override
    public SysParam toEntity(SysParamDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysParam.SysParamBuilder sysParam = SysParam.builder();

        sysParam.id( dto.getId() );
        sysParam.description( dto.getDescription() );
        sysParam.isDeleted( dto.getIsDeleted() );
        sysParam.updateTime( dto.getUpdateTime() );

        return sysParam.build();
    }

    @Override
    public List<SysParamDTO> toDTOList(List<SysParam> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<SysParamDTO> list = new ArrayList<SysParamDTO>( entityList.size() );
        for ( SysParam sysParam : entityList ) {
            list.add( toDTO( sysParam ) );
        }

        return list;
    }

    @Override
    public List<SysParam> toEntityList(List<SysParamDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<SysParam> list = new ArrayList<SysParam>( dtoList.size() );
        for ( SysParamDTO sysParamDTO : dtoList ) {
            list.add( toEntity( sysParamDTO ) );
        }

        return list;
    }
}
