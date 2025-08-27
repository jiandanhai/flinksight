package com.flinksight.backend.service;

import com.flinksight.backend.domain.JobInfo;
import com.flinksight.backend.mapper.JobInfoStructMapper;
import com.flinksight.backend.repository.JobInfoRepository;
import com.flinksight.common.dto.JobInfoDTO;
import com.flinksight.common.dto.JobRegisterRequestDTO;
import com.flinksight.common.service.JobPermissionService;
import com.flinksight.common.service.JobRegisterService;
import com.flinksight.common.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 作业注册服务实现类
 * 自动幂等、权限校验、平台权限联动
 */
@Service
@RequiredArgsConstructor
public class JobRegisterServiceImpl implements JobRegisterService {

    private final JobInfoRepository jobInfoRepository;
    private final PermissionService permissionService; // 平台权限Service
    private final JobPermissionService jobPermissionService; // 平台jOB权限Service
    private final JobInfoStructMapper jobInfoStructMapper;

    /**
     * 自动注册（平台幂等、权限联动、租户隔离）
     */
    @Transactional
    @Override
    public JobInfoDTO register(JobRegisterRequestDTO req) {
        // 幂等校验（traceId全局唯一，重复注册返回已有记录）
        Optional<JobInfo> opt = jobInfoRepository.findByTraceId(req.getTraceId());
        JobInfo job;
        if (opt.isPresent()) {
            job = opt.get();
        } else {
            // 租户/操作人权限校验
            permissionService.checkTenantRegisterPermission(req.getTenantId(), req.getOperator());

            // 新注册作业
            job = JobInfo.builder()
                    .jobName(req.getJobName())
                    .tenantId(req.getTenantId())
                    .jobType(req.getJobType())
                    .projectCode(req.getProjectCode())
                    .operator(req.getOperator())
                    .source(req.getSource())
                    .traceId(req.getTraceId())
                    .remark(req.getRemark())
                    .registerAt(req.getRegisterAt() != null ? req.getRegisterAt() : System.currentTimeMillis())
                    .isDeleted(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            job = jobInfoRepository.save(job);

            // 权限联动：为注册人/租户自动分配默认作业管理权限
            jobPermissionService.grantDefaultJobPermission(job.getId(), req.getOperator());
        }

        // DTO返回
        return jobInfoStructMapper.toDTO(job);
    }
}
