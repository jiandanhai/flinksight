package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.UserPost;
import com.flinksight.common.dto.UserPostDTO;
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
public class UserPostStructMapperImpl implements UserPostStructMapper {

    @Override
    public UserPostDTO toDTO(UserPost entity) {
        if ( entity == null ) {
            return null;
        }

        UserPostDTO.UserPostDTOBuilder userPostDTO = UserPostDTO.builder();

        userPostDTO.id( entity.getId() );
        userPostDTO.userId( entity.getUserId() );
        userPostDTO.postId( entity.getPostId() );
        userPostDTO.isDeleted( entity.getIsDeleted() );

        return userPostDTO.build();
    }

    @Override
    public UserPost toEntity(UserPostDTO dto) {
        if ( dto == null ) {
            return null;
        }

        UserPost.UserPostBuilder userPost = UserPost.builder();

        userPost.id( dto.getId() );
        userPost.userId( dto.getUserId() );
        userPost.postId( dto.getPostId() );
        userPost.isDeleted( dto.getIsDeleted() );

        return userPost.build();
    }

    @Override
    public List<UserPostDTO> toDTOList(List<UserPost> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<UserPostDTO> list = new ArrayList<UserPostDTO>( entityList.size() );
        for ( UserPost userPost : entityList ) {
            list.add( toDTO( userPost ) );
        }

        return list;
    }

    @Override
    public List<UserPost> toEntityList(List<UserPostDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<UserPost> list = new ArrayList<UserPost>( dtoList.size() );
        for ( UserPostDTO userPostDTO : dtoList ) {
            list.add( toEntity( userPostDTO ) );
        }

        return list;
    }
}
