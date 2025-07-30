package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.LabelLoginHistory;
import com.flinksight.common.dto.LabelLoginHistoryDTO;
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
public class LabelLoginHistoryStructMapperImpl implements LabelLoginHistoryStructMapper {

    @Override
    public LabelLoginHistoryDTO toDTO(LabelLoginHistory entity) {
        if ( entity == null ) {
            return null;
        }

        LabelLoginHistoryDTO.LabelLoginHistoryDTOBuilder labelLoginHistoryDTO = LabelLoginHistoryDTO.builder();

        labelLoginHistoryDTO.id( entity.getId() );
        labelLoginHistoryDTO.labelId( entity.getLabelId() );
        labelLoginHistoryDTO.userId( entity.getUserId() );
        labelLoginHistoryDTO.loginTime( entity.getLoginTime() );
        labelLoginHistoryDTO.ipAddress( entity.getIpAddress() );
        labelLoginHistoryDTO.tenantId( entity.getTenantId() );
        labelLoginHistoryDTO.isDeleted( entity.getIsDeleted() );

        return labelLoginHistoryDTO.build();
    }

    @Override
    public LabelLoginHistory toEntity(LabelLoginHistoryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        LabelLoginHistory.LabelLoginHistoryBuilder labelLoginHistory = LabelLoginHistory.builder();

        labelLoginHistory.id( dto.getId() );
        labelLoginHistory.labelId( dto.getLabelId() );
        labelLoginHistory.userId( dto.getUserId() );
        labelLoginHistory.loginTime( dto.getLoginTime() );
        labelLoginHistory.ipAddress( dto.getIpAddress() );
        labelLoginHistory.tenantId( dto.getTenantId() );
        labelLoginHistory.isDeleted( dto.getIsDeleted() );

        return labelLoginHistory.build();
    }

    @Override
    public List<LabelLoginHistoryDTO> toDTOList(List<LabelLoginHistory> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<LabelLoginHistoryDTO> list = new ArrayList<LabelLoginHistoryDTO>( entityList.size() );
        for ( LabelLoginHistory labelLoginHistory : entityList ) {
            list.add( toDTO( labelLoginHistory ) );
        }

        return list;
    }

    @Override
    public List<LabelLoginHistory> toEntityList(List<LabelLoginHistoryDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<LabelLoginHistory> list = new ArrayList<LabelLoginHistory>( dtoList.size() );
        for ( LabelLoginHistoryDTO labelLoginHistoryDTO : dtoList ) {
            list.add( toEntity( labelLoginHistoryDTO ) );
        }

        return list;
    }
}
