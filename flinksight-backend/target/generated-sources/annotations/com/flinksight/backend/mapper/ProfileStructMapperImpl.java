package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Profile;
import com.flinksight.common.dto.ProfileDTO;
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
public class ProfileStructMapperImpl implements ProfileStructMapper {

    @Override
    public ProfileDTO toDTO(Profile entity) {
        if ( entity == null ) {
            return null;
        }

        ProfileDTO.ProfileDTOBuilder profileDTO = ProfileDTO.builder();

        profileDTO.id( entity.getId() );
        profileDTO.userId( entity.getUserId() );
        profileDTO.isDeleted( entity.getIsDeleted() );

        return profileDTO.build();
    }

    @Override
    public Profile toEntity(ProfileDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Profile.ProfileBuilder profile = Profile.builder();

        profile.id( dto.getId() );
        profile.userId( dto.getUserId() );
        profile.isDeleted( dto.getIsDeleted() );

        return profile.build();
    }

    @Override
    public List<ProfileDTO> toDTOList(List<Profile> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ProfileDTO> list = new ArrayList<ProfileDTO>( entityList.size() );
        for ( Profile profile : entityList ) {
            list.add( toDTO( profile ) );
        }

        return list;
    }

    @Override
    public List<Profile> toEntityList(List<ProfileDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Profile> list = new ArrayList<Profile>( dtoList.size() );
        for ( ProfileDTO profileDTO : dtoList ) {
            list.add( toEntity( profileDTO ) );
        }

        return list;
    }
}
