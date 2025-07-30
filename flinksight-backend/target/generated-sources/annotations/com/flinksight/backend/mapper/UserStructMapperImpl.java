package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Role;
import com.flinksight.backend.domain.User;
import com.flinksight.common.dto.RoleDTO;
import com.flinksight.common.dto.UserDTO;
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
public class UserStructMapperImpl implements UserStructMapper {

    @Override
    public UserDTO toDTO(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.id( entity.getId() );
        userDTO.tenantId( entity.getTenantId() );
        userDTO.username( entity.getUsername() );
        userDTO.password( entity.getPassword() );
        userDTO.email( entity.getEmail() );
        userDTO.phone( entity.getPhone() );
        userDTO.status( entity.getStatus() );
        userDTO.isDeleted( entity.getIsDeleted() );
        userDTO.createTime( entity.getCreateTime() );
        userDTO.updateTime( entity.getUpdateTime() );
        userDTO.roles( roleListToRoleDTOList( entity.getRoles() ) );

        return userDTO.build();
    }

    @Override
    public User toEntity(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( dto.getId() );
        user.tenantId( dto.getTenantId() );
        user.username( dto.getUsername() );
        user.password( dto.getPassword() );
        user.email( dto.getEmail() );
        user.phone( dto.getPhone() );
        user.status( dto.getStatus() );
        user.isDeleted( dto.getIsDeleted() );
        user.createTime( dto.getCreateTime() );
        user.updateTime( dto.getUpdateTime() );
        user.roles( roleDTOListToRoleList( dto.getRoles() ) );

        return user.build();
    }

    @Override
    public List<UserDTO> toDTOList(List<User> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( entityList.size() );
        for ( User user : entityList ) {
            list.add( toDTO( user ) );
        }

        return list;
    }

    @Override
    public List<User> toEntityList(List<UserDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<User> list = new ArrayList<User>( dtoList.size() );
        for ( UserDTO userDTO : dtoList ) {
            list.add( toEntity( userDTO ) );
        }

        return list;
    }

    protected RoleDTO roleToRoleDTO(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleDTO.RoleDTOBuilder roleDTO = RoleDTO.builder();

        roleDTO.id( role.getId() );
        roleDTO.name( role.getName() );
        roleDTO.code( role.getCode() );
        roleDTO.desc( role.getDesc() );
        roleDTO.isDeleted( role.getIsDeleted() );

        return roleDTO.build();
    }

    protected List<RoleDTO> roleListToRoleDTOList(List<Role> list) {
        if ( list == null ) {
            return null;
        }

        List<RoleDTO> list1 = new ArrayList<RoleDTO>( list.size() );
        for ( Role role : list ) {
            list1.add( roleToRoleDTO( role ) );
        }

        return list1;
    }

    protected Role roleDTOToRole(RoleDTO roleDTO) {
        if ( roleDTO == null ) {
            return null;
        }

        Role.RoleBuilder role = Role.builder();

        role.id( roleDTO.getId() );
        role.name( roleDTO.getName() );
        role.code( roleDTO.getCode() );
        role.desc( roleDTO.getDesc() );
        role.isDeleted( roleDTO.getIsDeleted() );

        return role.build();
    }

    protected List<Role> roleDTOListToRoleList(List<RoleDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Role> list1 = new ArrayList<Role>( list.size() );
        for ( RoleDTO roleDTO : list ) {
            list1.add( roleDTOToRole( roleDTO ) );
        }

        return list1;
    }
}
