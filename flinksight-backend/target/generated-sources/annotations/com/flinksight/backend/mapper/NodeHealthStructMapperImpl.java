package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.NodeHealth;
import com.flinksight.common.dto.NodeHealthDTO;
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
public class NodeHealthStructMapperImpl implements NodeHealthStructMapper {

    @Override
    public NodeHealthDTO toDTO(NodeHealth entity) {
        if ( entity == null ) {
            return null;
        }

        NodeHealthDTO.NodeHealthDTOBuilder nodeHealthDTO = NodeHealthDTO.builder();

        nodeHealthDTO.id( entity.getId() );
        nodeHealthDTO.tenantId( entity.getTenantId() );
        nodeHealthDTO.nodeId( entity.getNodeId() );
        nodeHealthDTO.healthStatus( entity.getHealthStatus() );
        nodeHealthDTO.checkTime( entity.getCheckTime() );
        nodeHealthDTO.message( entity.getMessage() );
        nodeHealthDTO.isDeleted( entity.getIsDeleted() );

        return nodeHealthDTO.build();
    }

    @Override
    public NodeHealth toEntity(NodeHealthDTO dto) {
        if ( dto == null ) {
            return null;
        }

        NodeHealth.NodeHealthBuilder nodeHealth = NodeHealth.builder();

        nodeHealth.id( dto.getId() );
        nodeHealth.tenantId( dto.getTenantId() );
        nodeHealth.nodeId( dto.getNodeId() );
        nodeHealth.healthStatus( dto.getHealthStatus() );
        nodeHealth.checkTime( dto.getCheckTime() );
        nodeHealth.message( dto.getMessage() );
        nodeHealth.isDeleted( dto.getIsDeleted() );

        return nodeHealth.build();
    }

    @Override
    public List<NodeHealthDTO> toDTOList(List<NodeHealth> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<NodeHealthDTO> list = new ArrayList<NodeHealthDTO>( entityList.size() );
        for ( NodeHealth nodeHealth : entityList ) {
            list.add( toDTO( nodeHealth ) );
        }

        return list;
    }

    @Override
    public List<NodeHealth> toEntityList(List<NodeHealthDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<NodeHealth> list = new ArrayList<NodeHealth>( dtoList.size() );
        for ( NodeHealthDTO nodeHealthDTO : dtoList ) {
            list.add( toEntity( nodeHealthDTO ) );
        }

        return list;
    }
}
