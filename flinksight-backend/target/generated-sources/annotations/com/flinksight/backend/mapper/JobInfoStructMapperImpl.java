package com.flinksight.backend.mapper;

import com.flinksight.backend.domain.JobInfo;
import com.flinksight.common.dto.JobInfoDTO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class JobInfoStructMapperImpl implements JobInfoStructMapper {

    @Override
    public JobInfoDTO toDTO(JobInfo entity) {
        if ( entity == null ) {
            return null;
        }

        JobInfoDTO.JobInfoDTOBuilder jobInfoDTO = JobInfoDTO.builder();

        jobInfoDTO.id( entity.getId() );
        jobInfoDTO.jobName( entity.getJobName() );
        jobInfoDTO.tenantId( entity.getTenantId() );
        jobInfoDTO.jobType( entity.getJobType() );
        jobInfoDTO.projectCode( entity.getProjectCode() );
        jobInfoDTO.operator( entity.getOperator() );
        jobInfoDTO.source( entity.getSource() );
        jobInfoDTO.traceId( entity.getTraceId() );
        jobInfoDTO.remark( entity.getRemark() );
        jobInfoDTO.registerAt( entity.getRegisterAt() );
        jobInfoDTO.isDeleted( entity.getIsDeleted() );
        if ( entity.getCreatedAt() != null ) {
            jobInfoDTO.createdAt( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( entity.getCreatedAt() ) );
        }
        if ( entity.getUpdatedAt() != null ) {
            jobInfoDTO.updatedAt( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( entity.getUpdatedAt() ) );
        }

        return jobInfoDTO.build();
    }

    @Override
    public JobInfo toEntity(JobInfoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        JobInfo.JobInfoBuilder jobInfo = JobInfo.builder();

        jobInfo.id( dto.getId() );
        jobInfo.jobName( dto.getJobName() );
        jobInfo.tenantId( dto.getTenantId() );
        jobInfo.jobType( dto.getJobType() );
        jobInfo.projectCode( dto.getProjectCode() );
        jobInfo.operator( dto.getOperator() );
        jobInfo.source( dto.getSource() );
        jobInfo.traceId( dto.getTraceId() );
        jobInfo.remark( dto.getRemark() );
        jobInfo.registerAt( dto.getRegisterAt() );
        jobInfo.isDeleted( dto.getIsDeleted() );
        if ( dto.getCreatedAt() != null ) {
            jobInfo.createdAt( LocalDateTime.parse( dto.getCreatedAt() ) );
        }
        if ( dto.getUpdatedAt() != null ) {
            jobInfo.updatedAt( LocalDateTime.parse( dto.getUpdatedAt() ) );
        }

        return jobInfo.build();
    }

    @Override
    public List<JobInfoDTO> toDTOList(List<JobInfo> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<JobInfoDTO> list = new ArrayList<JobInfoDTO>( entityList.size() );
        for ( JobInfo jobInfo : entityList ) {
            list.add( toDTO( jobInfo ) );
        }

        return list;
    }

    @Override
    public List<JobInfo> toEntityList(List<JobInfoDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<JobInfo> list = new ArrayList<JobInfo>( dtoList.size() );
        for ( JobInfoDTO jobInfoDTO : dtoList ) {
            list.add( toEntity( jobInfoDTO ) );
        }

        return list;
    }
}
