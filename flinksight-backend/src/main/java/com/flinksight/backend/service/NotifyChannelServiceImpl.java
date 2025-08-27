package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.NotifyChannel;
import com.flinksight.backend.mapper.NotifyChannelMapper;
import com.flinksight.backend.repository.NotifyChannelRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.backend.security.tenant.TenantContext;
import com.flinksight.common.dto.NotifyChannelDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NotifyChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 通知渠道业务实现
 */
@Service
@RequiredArgsConstructor
public class NotifyChannelServiceImpl implements NotifyChannelService {

    private final NotifyChannelRepository repository;
    private final NotifyChannelMapper notifyChannelMapper;

    @Override
    @Transactional(readOnly = true)
    public NotifyChannelDTO create(NotifyChannelDTO dto) {
        NotifyChannel entity = notifyChannelMapper.toEntity(dto);
        entity.setIsDeleted(0);
        NotifyChannel saved = repository.save(entity);
        return notifyChannelMapper.toDTO(saved);
    }


    /** 更新（Patch：仅合并非 null 字段） */
    @Override
    @Transactional(readOnly = true)
    public NotifyChannelDTO update(Long id, NotifyChannelDTO patch) {
        Long tenantId = TenantContext.getTenantId();
        NotifyChannel one = repository.findByIdAndTenantIdAndIsDeleted(id, tenantId, 0)
                .orElseThrow(() -> new IllegalArgumentException("渠道不存在或不属于当前租户"));
        return notifyChannelMapper.toDTO(repository.save(one));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<NotifyChannelDTO> getMyById(Long id) {
        return repository.findByIdAndTenantIdAndIsDeleted(id, SecurityUtil.getCurrentTenantId(), 0)
                .map(notifyChannelMapper::toDTO);
    }

    /** 渠道列表（当前租户） */
    @Override
    @Transactional(readOnly = true)
    public PageResult<NotifyChannelDTO> list(int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, NotifyChannel.class); // 统一 1→0
        Page<NotifyChannel> result = repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),0, pr);
        return PageHelpers.toPageResult(result, notifyChannelMapper::toDTO, true); // 返回
    }


    @Override
    @Transactional(readOnly = true)
    public boolean sDelete(Long id) {
        int updated = repository.softDeleteByIdAndTenantId(id, SecurityUtil.getCurrentTenantId());
        if (updated == 0) {
            // 不存在 / 不属于该租户 / 已经被删除过
            throw new IllegalArgumentException("渠道不存在或不属于当前租户，或已删除");
        }
        return true; // 受影响=1 就表示软删成功

    }
}
