package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.TenantResource;
import com.flinksight.common.dto.TenantResourceDTO;
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
public class TenantResourceStructMapperImpl implements TenantResourceStructMapper {

    @Override
    public TenantResourceDTO toDTO(TenantResource entity) {
        if ( entity == null ) {
            return null;
        }

        TenantResourceDTO.TenantResourceDTOBuilder tenantResourceDTO = TenantResourceDTO.builder();

        tenantResourceDTO.id( entity.getId() );
        tenantResourceDTO.tenantId( entity.getTenantId() );
        tenantResourceDTO.resourceId( entity.getResourceId() );
        tenantResourceDTO.isDeleted( entity.getIsDeleted() );

        return tenantResourceDTO.build();
    }

    @Override
    public TenantResource toEntity(TenantResourceDTO dto) {
        if ( dto == null ) {
            return null;
        }

        TenantResource.TenantResourceBuilder tenantResource = TenantResource.builder();

        tenantResource.id( dto.getId() );
        tenantResource.tenantId( dto.getTenantId() );
        tenantResource.resourceId( dto.getResourceId() );
        tenantResource.isDeleted( dto.getIsDeleted() );

        return tenantResource.build();
    }

    @Override
    public List<TenantResourceDTO> toDTOList(List<TenantResource> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TenantResourceDTO> list = new ArrayList<TenantResourceDTO>( entityList.size() );
        for ( TenantResource tenantResource : entityList ) {
            list.add( toDTO( tenantResource ) );
        }

        return list;
    }

    @Override
    public List<TenantResource> toEntityList(List<TenantResourceDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<TenantResource> list = new ArrayList<TenantResource>( dtoList.size() );
        for ( TenantResourceDTO tenantResourceDTO : dtoList ) {
            list.add( toEntity( tenantResourceDTO ) );
        }

        return list;
    }
}
