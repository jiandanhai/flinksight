package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.JobPermission;
import com.flinksight.backend.domain.Permission;
import com.flinksight.backend.mapper.JobPermissionStructMapper;
import com.flinksight.backend.repository.JobPermissionRepository;
import com.flinksight.backend.repository.PermissionRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.JobPermissionDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.JobPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public void grantDefaultJobPermission(Long jobId,String operatorUserId) {
        // 假设“OWNER”权限编码，实际可按需求调整
        Permission ownerPerm = permissionRepository.findByCode("JOB_OWNER");
        if (ownerPerm == null) throw new RuntimeException("平台未配置作业OWNER权限");
        if (jobPermissionRepository.existsByTenantIdAndJobIdAndUserIdAndPermissionCode(SecurityUtil.getCurrentTenantId(),jobId, operatorUserId, ownerPerm.getCode())) {
            // 幂等校验
            return;
        }
        JobPermission perm = JobPermission.builder()
                .jobId(jobId)
                .tenantId(SecurityUtil.getCurrentTenantId())
                .userId(operatorUserId)
                .permissionCode(ownerPerm.getCode())
                .build();
        jobPermissionRepository.save(perm);
    }

    @Override
    public PageResult<JobPermissionDTO> list(Long jobId,int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, JobPermission.class); // 统一 1→0
        Page<JobPermission> result = jobPermissionRepository.findByTenantIdAndJobIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),jobId,0, pr);
        return PageHelpers.toPageResult(result, jobPermissionStructMapper::toDTO, true); // 返回 1
    }

    @Override
    @Transactional
    public void grantPermission(Long jobId, String userId, String permissionCode) {
        if (jobPermissionRepository.existsByTenantIdAndJobIdAndUserIdAndPermissionCode(SecurityUtil.getCurrentTenantId(),jobId, userId, permissionCode)) {
            throw new RuntimeException("用户已拥有该作业权限");
        }
        JobPermission perm = JobPermission.builder()
                .jobId(jobId)
                .tenantId(SecurityUtil.getCurrentTenantId())
                .userId(userId)
                .permissionCode(permissionCode)
                .build();
        jobPermissionRepository.save(perm);
    }

    @Override
    @Transactional
    public void revokePermission(Long jobId, String userId, String permissionCode) {
        jobPermissionRepository.deleteByTenantIdAndJobIdAndUserIdAndPermissionCode(SecurityUtil.getCurrentTenantId(),jobId, userId, permissionCode);
    }

    @Override
    public boolean hasJobPermission(Long jobId, Long userId) {
        // 可按实际平台“权限ID”配置判断（如owner/admin等权限ID可配置）        // 示例：只要有一条关联即认为有权限（可根据具体角色/权限进一步细化）
        return jobPermissionRepository.existsByTenantIdAndJobIdAndUserIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),jobId, String.valueOf(userId),0);
    }

    @Override
    public PageResult<JobPermissionDTO> getUserPermissions(Long jobId, String userId,int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, JobPermission.class); // 统一 1→0
        Page<JobPermission> result = jobPermissionRepository.findByTenantIdAndJobIdAndUserIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),jobId,userId,0,pr);
        return PageHelpers.toPageResult(result, jobPermissionStructMapper::toDTO, true); // 返回 1
    }

}
