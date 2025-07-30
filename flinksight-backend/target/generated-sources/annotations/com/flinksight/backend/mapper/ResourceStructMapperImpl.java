package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Resource;
import com.flinksight.common.dto.ResourceDTO;
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
public class ResourceStructMapperImpl implements ResourceStructMapper {

    @Override
    public ResourceDTO toDTO(Resource entity) {
        if ( entity == null ) {
            return null;
        }

        ResourceDTO.ResourceDTOBuilder resourceDTO = ResourceDTO.builder();

        resourceDTO.id( entity.getId() );
        resourceDTO.name( entity.getName() );
        resourceDTO.type( entity.getType() );
        resourceDTO.tenantId( entity.getTenantId() );
        resourceDTO.isDeleted( entity.getIsDeleted() );
        resourceDTO.createTime( entity.getCreateTime() );

        return resourceDTO.build();
    }

    @Override
    public Resource toEntity(ResourceDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Resource.ResourceBuilder resource = Resource.builder();

        resource.id( dto.getId() );
        resource.name( dto.getName() );
        resource.type( dto.getType() );
        resource.tenantId( dto.getTenantId() );
        resource.isDeleted( dto.getIsDeleted() );
        resource.createTime( dto.getCreateTime() );

        return resource.build();
    }

    @Override
    public List<ResourceDTO> toDTOList(List<Resource> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ResourceDTO> list = new ArrayList<ResourceDTO>( entityList.size() );
        for ( Resource resource : entityList ) {
            list.add( toDTO( resource ) );
        }

        return list;
    }

    @Override
    public List<Resource> toEntityList(List<ResourceDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Resource> list = new ArrayList<Resource>( dtoList.size() );
        for ( ResourceDTO resourceDTO : dtoList ) {
            list.add( toEntity( resourceDTO ) );
        }

        return list;
    }
}
