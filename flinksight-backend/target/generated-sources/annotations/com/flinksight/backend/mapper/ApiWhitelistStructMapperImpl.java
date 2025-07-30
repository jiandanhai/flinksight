package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.ApiWhitelist;
import com.flinksight.common.dto.ApiWhitelistDTO;
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
public class ApiWhitelistStructMapperImpl implements ApiWhitelistStructMapper {

    @Override
    public ApiWhitelistDTO toDTO(ApiWhitelist entity) {
        if ( entity == null ) {
            return null;
        }

        ApiWhitelistDTO apiWhitelistDTO = new ApiWhitelistDTO();

        apiWhitelistDTO.setId( entity.getId() );
        apiWhitelistDTO.setDescription( entity.getDescription() );
        apiWhitelistDTO.setIsDeleted( entity.getIsDeleted() );

        return apiWhitelistDTO;
    }

    @Override
    public ApiWhitelist toEntity(ApiWhitelistDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ApiWhitelist.ApiWhitelistBuilder apiWhitelist = ApiWhitelist.builder();

        apiWhitelist.id( dto.getId() );
        apiWhitelist.description( dto.getDescription() );
        apiWhitelist.isDeleted( dto.getIsDeleted() );

        return apiWhitelist.build();
    }

    @Override
    public List<ApiWhitelistDTO> toDTOList(List<ApiWhitelist> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ApiWhitelistDTO> list = new ArrayList<ApiWhitelistDTO>( entityList.size() );
        for ( ApiWhitelist apiWhitelist : entityList ) {
            list.add( toDTO( apiWhitelist ) );
        }

        return list;
    }

    @Override
    public List<ApiWhitelist> toEntityList(List<ApiWhitelistDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ApiWhitelist> list = new ArrayList<ApiWhitelist>( dtoList.size() );
        for ( ApiWhitelistDTO apiWhitelistDTO : dtoList ) {
            list.add( toEntity( apiWhitelistDTO ) );
        }

        return list;
    }
}
