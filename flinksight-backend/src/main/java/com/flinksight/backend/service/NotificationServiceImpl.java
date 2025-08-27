package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.Notification;
import com.flinksight.backend.mapper.NotificationStructMapper;
import com.flinksight.backend.repository.NotificationRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.NotificationDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final NotificationStructMapper notificationStructMapper;

    @Override
    public NotificationDTO createOrUpdate(NotificationDTO notificationDTO) {
        Notification entity = notificationStructMapper.toEntity(notificationDTO);
        entity.setIsDeleted(0);
        Notification saved = repository.save(entity);
        return notificationStructMapper.toDTO(saved);
    }


    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Optional<NotificationDTO> getMyById(Long id) {
        Optional<Notification> result = repository.findByIdAndUserIdAndTenantIdAndIsDeleted(id, SecurityUtil.getCurrentUserId(), SecurityUtil.getCurrentTenantId(), 0);
        return result.map(notificationStructMapper::toDTO);
    }

    @Override
    public PageResult<NotificationDTO> findByTenantId(int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, Notification.class); // 统一 1→0
        Page<Notification> result = repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),0, pr);
        return PageHelpers.toPageResult(result, notificationStructMapper::toDTO, true); // 返回
    }


    /** 消息列表（当前用户 + 当前租户），支持按类型/已读筛选 */
    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public PageResult<NotificationDTO> list(String type, Integer isRead, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, Notification.class); // 统一 1→0
        Page<Notification> result;
        if (type != null && !type.isBlank()) {
            result = repository.findByUserIdAndTenantIdAndIsDeletedAndType(SecurityUtil.getCurrentUserId(), SecurityUtil.getCurrentTenantId(), 0, type, pr);
        } else if (isRead != null) {
            result = repository.findByUserIdAndTenantIdAndIsDeletedAndIsRead(SecurityUtil.getCurrentUserId(), SecurityUtil.getCurrentTenantId(), 0, isRead, pr);
        } else {
            result = repository.findByUserIdAndTenantIdAndIsDeleted(SecurityUtil.getCurrentUserId(), SecurityUtil.getCurrentTenantId(), 0, pr);
        }
        return PageHelpers.toPageResult(result, notificationStructMapper::toDTO, true); // 返回
    }


    /** 批量标记已读（只改属于当前用户+租户的消息） */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public int markRead(List<Long> ids) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        Long userId = SecurityUtil.getCurrentUserId();
        List<Notification> list = repository.findByIdInAndUserIdAndTenantIdAndIsDeleted(ids, userId, tenantId, 0);
        list.forEach(n -> n.setIsRead(1));
        repository.saveAll(list);
        return list.size();
    }


    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public boolean sDelete(Long id) {
        int updated = repository.softDeleteByIdAndOwner(id, SecurityUtil.getCurrentUserId(),SecurityUtil.getCurrentTenantId());
        if (updated == 0) {
            // 不存在 / 不属于该租户 / 已经被删除过
            throw new IllegalArgumentException("渠道不存在或不属于当前租户，或已删除");
        }
        return true; // 受影响=1 就表示软删成功
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public int softDelete(List<Long> ids) {
        return repository.softDeleteByIdsAndOwner(ids, SecurityUtil.getCurrentUserId(),SecurityUtil.getCurrentTenantId());
    }
}
