package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserGroup;
import com.flinksight.common.dto.UserGroupDTO;
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
public class UserGroupStructMapperImpl implements UserGroupStructMapper {

    @Override
    public UserGroupDTO toDTO(UserGroup entity) {
        if ( entity == null ) {
            return null;
        }

        UserGroupDTO.UserGroupDTOBuilder userGroupDTO = UserGroupDTO.builder();

        userGroupDTO.id( entity.getId() );
        userGroupDTO.userId( entity.getUserId() );
        userGroupDTO.groupId( entity.getGroupId() );
        userGroupDTO.isDeleted( entity.getIsDeleted() );

        return userGroupDTO.build();
    }

    @Override
    public UserGroup toEntity(UserGroupDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserGroup.UserGroupBuilder userGroup = UserGroup.builder();

        userGroup.id( dto.getId() );
        userGroup.userId( dto.getUserId() );
        userGroup.groupId( dto.getGroupId() );
        userGroup.isDeleted( dto.getIsDeleted() );

        return userGroup.build();
    }

    @Override
    public List<UserGroupDTO> toDTOList(List<UserGroup> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<UserGroupDTO> list = new ArrayList<UserGroupDTO>( entityList.size() );
        for ( UserGroup userGroup : entityList ) {
            list.add( toDTO( userGroup ) );
        }

        return list;
    }

    @Override
    public List<UserGroup> toEntityList(List<UserGroupDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<UserGroup> list = new ArrayList<UserGroup>( dtoList.size() );
        for ( UserGroupDTO userGroupDTO : dtoList ) {
            list.add( toEntity( userGroupDTO ) );
        }

        return list;
    }
}
