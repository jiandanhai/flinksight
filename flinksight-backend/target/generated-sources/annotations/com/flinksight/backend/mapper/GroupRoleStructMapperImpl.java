package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.GroupRole;
import com.flinksight.common.dto.GroupRoleDTO;
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
public class GroupRoleStructMapperImpl implements GroupRoleStructMapper {

    @Override
    public GroupRoleDTO toDTO(GroupRole entity) {
        if ( entity == null ) {
            return null;
        }

        GroupRoleDTO.GroupRoleDTOBuilder groupRoleDTO = GroupRoleDTO.builder();

        groupRoleDTO.id( entity.getId() );
        groupRoleDTO.groupId( entity.getGroupId() );
        groupRoleDTO.roleId( entity.getRoleId() );
        groupRoleDTO.isDeleted( entity.getIsDeleted() );

        return groupRoleDTO.build();
    }

    @Override
    public GroupRole toEntity(GroupRoleDTO dto) {
        if ( dto == null ) {
            return null;
        }

        GroupRole.GroupRoleBuilder groupRole = GroupRole.builder();

        groupRole.id( dto.getId() );
        groupRole.groupId( dto.getGroupId() );
        groupRole.roleId( dto.getRoleId() );
        groupRole.isDeleted( dto.getIsDeleted() );

        return groupRole.build();
    }

    @Override
    public List<GroupRoleDTO> toDTOList(List<GroupRole> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<GroupRoleDTO> list = new ArrayList<GroupRoleDTO>( entityList.size() );
        for ( GroupRole groupRole : entityList ) {
            list.add( toDTO( groupRole ) );
        }

        return list;
    }

    @Override
    public List<GroupRole> toEntityList(List<GroupRoleDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<GroupRole> list = new ArrayList<GroupRole>( dtoList.size() );
        for ( GroupRoleDTO groupRoleDTO : dtoList ) {
            list.add( toEntity( groupRoleDTO ) );
        }

        return list;
    }
}
