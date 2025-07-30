package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.AlertRule;
import com.flinksight.common.dto.AlertRuleDTO;
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
public class AlertRuleStructMapperImpl implements AlertRuleStructMapper {

    @Override
    public AlertRuleDTO toDTO(AlertRule entity) {
        if ( entity == null ) {
            return null;
        }

        AlertRuleDTO.AlertRuleDTOBuilder alertRuleDTO = AlertRuleDTO.builder();

        alertRuleDTO.id( entity.getId() );
        alertRuleDTO.tenantId( entity.getTenantId() );
        alertRuleDTO.clusterId( entity.getClusterId() );
        alertRuleDTO.metricKey( entity.getMetricKey() );
        alertRuleDTO.threshold( entity.getThreshold() );
        alertRuleDTO.compareOp( entity.getCompareOp() );
        alertRuleDTO.channel( entity.getChannel() );
        alertRuleDTO.enable( entity.getEnable() );
        alertRuleDTO.isDeleted( entity.getIsDeleted() );
        alertRuleDTO.createTime( entity.getCreateTime() );

        return alertRuleDTO.build();
    }

    @Override
    public AlertRule toEntity(AlertRuleDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AlertRule.AlertRuleBuilder alertRule = AlertRule.builder();

        alertRule.id( dto.getId() );
        alertRule.tenantId( dto.getTenantId() );
        alertRule.clusterId( dto.getClusterId() );
        alertRule.metricKey( dto.getMetricKey() );
        alertRule.threshold( dto.getThreshold() );
        alertRule.compareOp( dto.getCompareOp() );
        alertRule.channel( dto.getChannel() );
        alertRule.enable( dto.getEnable() );
        alertRule.isDeleted( dto.getIsDeleted() );
        alertRule.createTime( dto.getCreateTime() );

        return alertRule.build();
    }

    @Override
    public List<AlertRuleDTO> toDTOList(List<AlertRule> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<AlertRuleDTO> list = new ArrayList<AlertRuleDTO>( entityList.size() );
        for ( AlertRule alertRule : entityList ) {
            list.add( toDTO( alertRule ) );
        }

        return list;
    }

    @Override
    public List<AlertRule> toEntityList(List<AlertRuleDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<AlertRule> list = new ArrayList<AlertRule>( dtoList.size() );
        for ( AlertRuleDTO alertRuleDTO : dtoList ) {
            list.add( toEntity( alertRuleDTO ) );
        }

        return list;
    }
}
