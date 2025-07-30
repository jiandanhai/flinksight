package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobAlertRule;
import com.flinksight.common.dto.JobAlertRuleDTO;
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
public class JobAlertRuleStructMapperImpl implements JobAlertRuleStructMapper {

    @Override
    public JobAlertRuleDTO toDTO(JobAlertRule entity) {
        if ( entity == null ) {
            return null;
        }

        JobAlertRuleDTO.JobAlertRuleDTOBuilder jobAlertRuleDTO = JobAlertRuleDTO.builder();

        jobAlertRuleDTO.id( entity.getId() );
        jobAlertRuleDTO.alertType( entity.getAlertType() );
        jobAlertRuleDTO.tenantId( entity.getTenantId() );
        jobAlertRuleDTO.isDeleted( entity.getIsDeleted() );

        return jobAlertRuleDTO.build();
    }

    @Override
    public JobAlertRule toEntity(JobAlertRuleDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobAlertRule.JobAlertRuleBuilder jobAlertRule = JobAlertRule.builder();

        jobAlertRule.id( dto.getId() );
        jobAlertRule.tenantId( dto.getTenantId() );
        jobAlertRule.alertType( dto.getAlertType() );
        jobAlertRule.isDeleted( dto.getIsDeleted() );

        return jobAlertRule.build();
    }

    @Override
    public List<JobAlertRuleDTO> toDTOList(List<JobAlertRule> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<JobAlertRuleDTO> list = new ArrayList<JobAlertRuleDTO>( entityList.size() );
        for ( JobAlertRule jobAlertRule : entityList ) {
            list.add( toDTO( jobAlertRule ) );
        }

        return list;
    }

    @Override
    public List<JobAlertRule> toEntityList(List<JobAlertRuleDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<JobAlertRule> list = new ArrayList<JobAlertRule>( dtoList.size() );
        for ( JobAlertRuleDTO jobAlertRuleDTO : dtoList ) {
            list.add( toEntity( jobAlertRuleDTO ) );
        }

        return list;
    }
}
