package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Tenant;
import com.flinksight.common.dto.TenantDTO;
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
public class TenantStructMapperImpl implements TenantStructMapper {

    @Override
    public TenantDTO toDTO(Tenant entity) {
        if ( entity == null ) {
            return null;
        }

        TenantDTO.TenantDTOBuilder tenantDTO = TenantDTO.builder();

        tenantDTO.id( entity.getId() );
        tenantDTO.name( entity.getName() );
        tenantDTO.code( entity.getCode() );
        tenantDTO.contact( entity.getContact() );
        tenantDTO.status( entity.getStatus() );
        tenantDTO.createTime( entity.getCreateTime() );
        tenantDTO.isDeleted( entity.getIsDeleted() );

        return tenantDTO.build();
    }

    @Override
    public Tenant toEntity(TenantDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Tenant.TenantBuilder tenant = Tenant.builder();

        tenant.id( dto.getId() );
        tenant.name( dto.getName() );
        tenant.code( dto.getCode() );
        tenant.contact( dto.getContact() );
        tenant.status( dto.getStatus() );
        tenant.createTime( dto.getCreateTime() );
        tenant.isDeleted( dto.getIsDeleted() );

        return tenant.build();
    }

    @Override
    public List<TenantDTO> toDTOList(List<Tenant> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<TenantDTO> list = new ArrayList<TenantDTO>( entityList.size() );
        for ( Tenant tenant : entityList ) {
            list.add( toDTO( tenant ) );
        }

        return list;
    }

    @Override
    public List<Tenant> toEntityList(List<TenantDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Tenant> list = new ArrayList<Tenant>( dtoList.size() );
        for ( TenantDTO tenantDTO : dtoList ) {
            list.add( toEntity( tenantDTO ) );
        }

        return list;
    }
}
