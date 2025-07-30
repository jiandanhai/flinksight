package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.AlertHistory;
import com.flinksight.common.dto.AlertHistoryDTO;
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
public class AlertHistoryStructMapperImpl implements AlertHistoryStructMapper {

    @Override
    public AlertHistoryDTO toDTO(AlertHistory entity) {
        if ( entity == null ) {
            return null;
        }

        AlertHistoryDTO alertHistoryDTO = new AlertHistoryDTO();

        alertHistoryDTO.setId( entity.getId() );
        alertHistoryDTO.setAlertId( entity.getAlertId() );
        alertHistoryDTO.setRuleId( entity.getRuleId() );
        alertHistoryDTO.setContent( entity.getContent() );
        alertHistoryDTO.setLevel( entity.getLevel() );
        alertHistoryDTO.setStatus( entity.getStatus() );
        alertHistoryDTO.setOperatorId( entity.getOperatorId() );
        alertHistoryDTO.setTenantId( entity.getTenantId() );
        alertHistoryDTO.setOperateTime( entity.getOperateTime() );
        alertHistoryDTO.setIsDeleted( entity.getIsDeleted() );
        alertHistoryDTO.setCreateTime( entity.getCreateTime() );

        return alertHistoryDTO;
    }

    @Override
    public AlertHistory toEntity(AlertHistoryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AlertHistory.AlertHistoryBuilder alertHistory = AlertHistory.builder();

        alertHistory.id( dto.getId() );
        alertHistory.alertId( dto.getAlertId() );
        alertHistory.ruleId( dto.getRuleId() );
        alertHistory.content( dto.getContent() );
        alertHistory.level( dto.getLevel() );
        alertHistory.status( dto.getStatus() );
        alertHistory.operatorId( dto.getOperatorId() );
        alertHistory.tenantId( dto.getTenantId() );
        alertHistory.operateTime( dto.getOperateTime() );
        alertHistory.isDeleted( dto.getIsDeleted() );
        alertHistory.createTime( dto.getCreateTime() );

        return alertHistory.build();
    }

    @Override
    public List<AlertHistoryDTO> toDTOList(List<AlertHistory> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AlertHistoryDTO> list = new ArrayList<AlertHistoryDTO>( entityList.size() );
        for ( AlertHistory alertHistory : entityList ) {
            list.add( toDTO( alertHistory ) );
        }

        return list;
    }

    @Override
    public List<AlertHistory> toEntityList(List<AlertHistoryDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AlertHistory> list = new ArrayList<AlertHistory>( dtoList.size() );
        for ( AlertHistoryDTO alertHistoryDTO : dtoList ) {
            list.add( toEntity( alertHistoryDTO ) );
        }

        return list;
    }
}
