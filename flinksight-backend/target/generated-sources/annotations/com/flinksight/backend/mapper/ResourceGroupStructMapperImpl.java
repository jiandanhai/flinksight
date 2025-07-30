package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.ResourceGroup;
import com.flinksight.common.dto.ResourceGroupDTO;
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
public class ResourceGroupStructMapperImpl implements ResourceGroupStructMapper {

    @Override
    public ResourceGroupDTO toDTO(ResourceGroup entity) {
        if ( entity == null ) {
            return null;
        }

        ResourceGroupDTO.ResourceGroupDTOBuilder resourceGroupDTO = ResourceGroupDTO.builder();

        resourceGroupDTO.id( entity.getId() );
        resourceGroupDTO.name( entity.getName() );
        resourceGroupDTO.type( entity.getType() );
        resourceGroupDTO.parentId( entity.getParentId() );
        resourceGroupDTO.tenantId( entity.getTenantId() );
        resourceGroupDTO.description( entity.getDescription() );
        resourceGroupDTO.createTime( entity.getCreateTime() );
        resourceGroupDTO.isDeleted( entity.getIsDeleted() );

        return resourceGroupDTO.build();
    }

    @Override
    public ResourceGroup toEntity(ResourceGroupDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ResourceGroup.ResourceGroupBuilder resourceGroup = ResourceGroup.builder();

        resourceGroup.id( dto.getId() );
        resourceGroup.name( dto.getName() );
        resourceGroup.type( dto.getType() );
        resourceGroup.parentId( dto.getParentId() );
        resourceGroup.tenantId( dto.getTenantId() );
        resourceGroup.description( dto.getDescription() );
        resourceGroup.createTime( dto.getCreateTime() );
        resourceGroup.isDeleted( dto.getIsDeleted() );

        return resourceGroup.build();
    }

    @Override
    public List<ResourceGroupDTO> toDTOList(List<ResourceGroup> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ResourceGroupDTO> list = new ArrayList<ResourceGroupDTO>( entityList.size() );
        for ( ResourceGroup resourceGroup : entityList ) {
            list.add( toDTO( resourceGroup ) );
        }

        return list;
    }

    @Override
    public List<ResourceGroup> toEntityList(List<ResourceGroupDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ResourceGroup> list = new ArrayList<ResourceGroup>( dtoList.size() );
        for ( ResourceGroupDTO resourceGroupDTO : dtoList ) {
            list.add( toEntity( resourceGroupDTO ) );
        }

        return list;
    }
}
