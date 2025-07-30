package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserApi;
import com.flinksight.common.dto.UserApiDTO;
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
public class UserApiStructMapperImpl implements UserApiStructMapper {

    @Override
    public UserApiDTO toDTO(UserApi entity) {
        if ( entity == null ) {
            return null;
        }

        UserApiDTO.UserApiDTOBuilder userApiDTO = UserApiDTO.builder();

        userApiDTO.id( entity.getId() );
        userApiDTO.userId( entity.getUserId() );
        userApiDTO.apiId( entity.getApiId() );
        userApiDTO.isDeleted( entity.getIsDeleted() );

        return userApiDTO.build();
    }

    @Override
    public UserApi toEntity(UserApiDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserApi.UserApiBuilder userApi = UserApi.builder();

        userApi.id( dto.getId() );
        userApi.userId( dto.getUserId() );
        userApi.apiId( dto.getApiId() );
        userApi.isDeleted( dto.getIsDeleted() );

        return userApi.build();
    }

    @Override
    public List<UserApiDTO> toDTOList(List<UserApi> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<UserApiDTO> list = new ArrayList<UserApiDTO>( entityList.size() );
        for ( UserApi userApi : entityList ) {
            list.add( toDTO( userApi ) );
        }

        return list;
    }

    @Override
    public List<UserApi> toEntityList(List<UserApiDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<UserApi> list = new ArrayList<UserApi>( dtoList.size() );
        for ( UserApiDTO userApiDTO : dtoList ) {
            list.add( toEntity( userApiDTO ) );
        }

        return list;
    }
}
