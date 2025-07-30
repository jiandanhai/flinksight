package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.ClusterStatusHistory;
import com.flinksight.common.dto.ClusterStatusHistoryDTO;
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
public class ClusterStatusHistoryStructMapperImpl implements ClusterStatusHistoryStructMapper {

    @Override
    public ClusterStatusHistoryDTO toDTO(ClusterStatusHistory entity) {
        if ( entity == null ) {
            return null;
        }

        ClusterStatusHistoryDTO.ClusterStatusHistoryDTOBuilder clusterStatusHistoryDTO = ClusterStatusHistoryDTO.builder();

        clusterStatusHistoryDTO.id( entity.getId() );
        clusterStatusHistoryDTO.clusterId( entity.getClusterId() );
        clusterStatusHistoryDTO.isDeleted( entity.getIsDeleted() );
        clusterStatusHistoryDTO.collectTime( entity.getCollectTime() );
        clusterStatusHistoryDTO.activeNodeCount( entity.getActiveNodeCount() );
        clusterStatusHistoryDTO.cpuUsage( entity.getCpuUsage() );
        clusterStatusHistoryDTO.memoryUsage( entity.getMemoryUsage() );
        clusterStatusHistoryDTO.queueLoadJson( entity.getQueueLoadJson() );
        clusterStatusHistoryDTO.extendJson( entity.getExtendJson() );

        return clusterStatusHistoryDTO.build();
    }

    @Override
    public ClusterStatusHistory toEntity(ClusterStatusHistoryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ClusterStatusHistory.ClusterStatusHistoryBuilder clusterStatusHistory = ClusterStatusHistory.builder();

        clusterStatusHistory.id( dto.getId() );
        clusterStatusHistory.clusterId( dto.getClusterId() );
        clusterStatusHistory.collectTime( dto.getCollectTime() );
        clusterStatusHistory.activeNodeCount( dto.getActiveNodeCount() );
        clusterStatusHistory.cpuUsage( dto.getCpuUsage() );
        clusterStatusHistory.memoryUsage( dto.getMemoryUsage() );
        clusterStatusHistory.queueLoadJson( dto.getQueueLoadJson() );
        clusterStatusHistory.extendJson( dto.getExtendJson() );
        clusterStatusHistory.isDeleted( dto.getIsDeleted() );

        return clusterStatusHistory.build();
    }

    @Override
    public List<ClusterStatusHistoryDTO> toDTOList(List<ClusterStatusHistory> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ClusterStatusHistoryDTO> list = new ArrayList<ClusterStatusHistoryDTO>( entityList.size() );
        for ( ClusterStatusHistory clusterStatusHistory : entityList ) {
            list.add( toDTO( clusterStatusHistory ) );
        }

        return list;
    }

    @Override
    public List<ClusterStatusHistory> toEntityList(List<ClusterStatusHistoryDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<ClusterStatusHistory> list = new ArrayList<ClusterStatusHistory>( dtoList.size() );
        for ( ClusterStatusHistoryDTO clusterStatusHistoryDTO : dtoList ) {
            list.add( toEntity( clusterStatusHistoryDTO ) );
        }

        return list;
    }
}
