package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.Node;
import com.flinksight.common.dto.NodeDTO;
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
public class NodeStructMapperImpl implements NodeStructMapper {

    @Override
    public NodeDTO toDTO(Node entity) {
        if ( entity == null ) {
            return null;
        }

        NodeDTO.NodeDTOBuilder nodeDTO = NodeDTO.builder();

        nodeDTO.id( entity.getId() );
        nodeDTO.name( entity.getName() );
        nodeDTO.type( entity.getType() );
        nodeDTO.ip( entity.getIp() );
        nodeDTO.clusterId( entity.getClusterId() );
        nodeDTO.status( entity.getStatus() );
        nodeDTO.isDeleted( entity.getIsDeleted() );
        nodeDTO.createTime( entity.getCreateTime() );

        return nodeDTO.build();
    }

    @Override
    public Node toEntity(NodeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Node.NodeBuilder node = Node.builder();

        node.id( dto.getId() );
        node.name( dto.getName() );
        node.type( dto.getType() );
        node.ip( dto.getIp() );
        node.clusterId( dto.getClusterId() );
        node.status( dto.getStatus() );
        node.isDeleted( dto.getIsDeleted() );
        node.createTime( dto.getCreateTime() );

        return node.build();
    }

    @Override
    public List<NodeDTO> toDTOList(List<Node> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<NodeDTO> list = new ArrayList<NodeDTO>( entityList.size() );
        for ( Node node : entityList ) {
            list.add( toDTO( node ) );
        }

        return list;
    }

    @Override
    public List<Node> toEntityList(List<NodeDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Node> list = new ArrayList<Node>( dtoList.size() );
        for ( NodeDTO nodeDTO : dtoList ) {
            list.add( toEntity( nodeDTO ) );
        }

        return list;
    }
}
