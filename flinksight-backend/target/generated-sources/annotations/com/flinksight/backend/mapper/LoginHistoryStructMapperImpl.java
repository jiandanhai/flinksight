package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.LoginHistory;
import com.flinksight.common.dto.LoginHistoryDTO;
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
public class LoginHistoryStructMapperImpl implements LoginHistoryStructMapper {

    @Override
    public LoginHistoryDTO toDTO(LoginHistory entity) {
        if ( entity == null ) {
            return null;
        }

        LoginHistoryDTO.LoginHistoryDTOBuilder loginHistoryDTO = LoginHistoryDTO.builder();

        loginHistoryDTO.id( entity.getId() );
        loginHistoryDTO.userId( entity.getUserId() );
        loginHistoryDTO.ip( entity.getIp() );
        loginHistoryDTO.loginTime( entity.getLoginTime() );
        loginHistoryDTO.isDeleted( entity.getIsDeleted() );

        return loginHistoryDTO.build();
    }

    @Override
    public LoginHistory toEntity(LoginHistoryDTO dto) {
        if ( dto == null ) {
            return null;
        }

        LoginHistory.LoginHistoryBuilder loginHistory = LoginHistory.builder();

        loginHistory.id( dto.getId() );
        loginHistory.userId( dto.getUserId() );
        loginHistory.ip( dto.getIp() );
        loginHistory.loginTime( dto.getLoginTime() );
        loginHistory.isDeleted( dto.getIsDeleted() );

        return loginHistory.build();
    }

    @Override
    public List<LoginHistoryDTO> toDTOList(List<LoginHistory> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<LoginHistoryDTO> list = new ArrayList<LoginHistoryDTO>( entityList.size() );
        for ( LoginHistory loginHistory : entityList ) {
            list.add( toDTO( loginHistory ) );
        }

        return list;
    }

    @Override
    public List<LoginHistory> toEntityList(List<LoginHistoryDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<LoginHistory> list = new ArrayList<LoginHistory>( dtoList.size() );
        for ( LoginHistoryDTO loginHistoryDTO : dtoList ) {
            list.add( toEntity( loginHistoryDTO ) );
        }

        return list;
    }
}
