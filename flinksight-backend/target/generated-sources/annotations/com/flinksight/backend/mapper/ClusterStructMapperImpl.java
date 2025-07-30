package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Cluster;
import com.flinksight.common.dto.ClusterDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T19:25:52+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Oracle Corporation)"
)
@Component
public class ClusterStructMapperImpl implements ClusterStructMapper {

    @Override
    public ClusterDTO toDTO(Cluster entity) {
        if ( entity == null ) {
            return null;
        }

        ClusterDTO.ClusterDTOBuilder clusterDTO = ClusterDTO.builder();

        clusterDTO.id( entity.getId() );
        clusterDTO.tenantId( entity.getTenantId() );
        clusterDTO.name( entity.getName() );
        clusterDTO.type( entity.getType() );
        clusterDTO.endpoint( entity.getEndpoint() );
        clusterDTO.version( entity.getVersion() );
        clusterDTO.tags( entity.getTags() );
        clusterDTO.status( entity.getStatus() );
        clusterDTO.remark( entity.getRemark() );
        clusterDTO.isDeleted( entity.getIsDeleted() );
        clusterDTO.createTime( entity.getCreateTime() );

        return clusterDTO.build();
    }

    @Override
    public Cluster toEntity(ClusterDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Cluster.ClusterBuilder cluster = Cluster.builder();

        cluster.id( dto.getId() );
        cluster.tenantId( dto.getTenantId() );
        cluster.name( dto.getName() );
        cluster.type( dto.getType() );
        cluster.endpoint( dto.getEndpoint() );
        cluster.version( dto.getVersion() );
        cluster.tags( dto.getTags() );
        cluster.status( dto.getStatus() );
        cluster.remark( dto.getRemark() );
        cluster.isDeleted( dto.getIsDeleted() );
        cluster.createTime( dto.getCreateTime() );

        return cluster.build();
    }

    @Override
    public List<ClusterDTO> toDTOList(List<Cluster> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ClusterDTO> list = new ArrayList<ClusterDTO>( entityList.size() );
        for ( Cluster cluster : entityList ) {
            list.add( toDTO( cluster ) );
        }

        return list;
    }

    @Override
    public List<Cluster> toEntityList(List<ClusterDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Cluster> list = new ArrayList<Cluster>( dtoList.size() );
        for ( ClusterDTO clusterDTO : dtoList ) {
            list.add( toEntity( clusterDTO ) );
        }

        return list;
    }
}
