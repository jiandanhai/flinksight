package com.flinksight.backend.service;

import com.flinksight.backend.domain.JobPermission;
import com.flinksight.backend.domain.Permission;
import com.flinksight.backend.mapper.JobPermissionStructMapper;
import com.flinksight.backend.repository.JobPermissionRepository;
import com.flinksight.backend.repository.PermissionRepository;
import com.flinksight.common.dto.JobPermissionDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作业-权限分配服务实现
 */
@Service
@RequiredArgsConstructor
public class JobPermissionServiceImpl implements JobPermissionService {

    private final JobPermissionRepository jobPermissionRepository;
    private final PermissionRepository permissionRepository;
    private final JobPermissionStructMapper jobPermissionStructMapper;

    @Override
    @Transactional
    public void grantDefaultJobPermission(Long jobId, Long tenantId, String operatorUserId) {
        // 假设“OWNER”权限编码，实际可按需求调整
        Permission ownerPerm = permissionRepository.findByCode("JOB_OWNER");
        if (ownerPerm == null) throw new RuntimeException("平台未配置作业OWNER权限");
        if (jobPermissionRepository.existsByJobIdAndUserIdAndPermissionCode(jobId, operatorUserId, ownerPerm.getCode())) {
            // 幂等校验
            return;
        }
        JobPermission perm = JobPermission.builder()
                .jobId(jobId)
                .tenantId(tenantId)
                .userId(operatorUserId)
                .permissionCode(ownerPerm.getCode())
                .build();
        jobPermissionRepository.save(perm);
    }

    @Override
    public PageResult<JobPermissionDTO> listJobPermissions(Long jobId,int page, int size) {
        Page<JobPermission> result = jobPermissionRepository.findByJobIdAndIsDeleted(jobId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<JobPermissionDTO> dtoPage = result.map(jobPermissionStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    @Transactional
    public void grantPermission(Long jobId, Long tenantId, String userId, String permissionCode) {
        if (jobPermissionRepository.existsByJobIdAndUserIdAndPermissionCode(jobId, userId, permissionCode)) {
            throw new RuntimeException("用户已拥有该作业权限");
        }
        JobPermission perm = JobPermission.builder()
                .jobId(jobId)
                .tenantId(tenantId)
                .userId(userId)
                .permissionCode(permissionCode)
                .build();
        jobPermissionRepository.save(perm);
    }

    @Override
    @Transactional
    public void revokePermission(Long jobId, String userId, String permissionCode) {
        jobPermissionRepository.deleteByJobIdAndUserIdAndPermissionCode(jobId, userId, permissionCode);
    }

    @Override
    public boolean hasJobPermission(Long jobId, Long userId) {
        // 可按实际平台“权限ID”配置判断（如owner/admin等权限ID可配置）        // 示例：只要有一条关联即认为有权限（可根据具体角色/权限进一步细化）
        return jobPermissionRepository.existsByJobIdAndUserIdAndIsDeleted(jobId, String.valueOf(userId),0);
    }

    @Override
    public PageResult<JobPermissionDTO> getUserPermissions(Long jobId, String userId,int page, int size) {
        Page<JobPermission> result = jobPermissionRepository.findByJobIdAndUserIdAndIsDeleted(jobId,userId,0,PageRequest.of(page, size, Sort.by("id").descending()));
        Page<JobPermissionDTO> dtoPage = result.map(jobPermissionStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    private JobPermissionDTO toDTO(JobPermission perm) {
        Permission permission = permissionRepository.findByCode(perm.getPermissionCode());
        return JobPermissionDTO.builder()
                .id(perm.getId())
                .jobId(perm.getJobId())
                .tenantId(perm.getTenantId())
                .userId(perm.getUserId())
                .permissionCode(perm.getPermissionCode())
                .permissionCode(permission != null ? permission.getCode() : null)
                .permissionName(permission != null ? permission.getName() : null)
                .build();
    }
}
