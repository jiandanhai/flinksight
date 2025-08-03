package com.flinksight.backend.service;

import com.flinksight.backend.domain.Notification;
import com.flinksight.backend.mapper.NotificationStructMapper;
import com.flinksight.backend.repository.NotificationRepository;
import com.flinksight.common.dto.NotificationDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public Optional<NotificationDTO> getById(Long id) {
        return repository.findById(id).map(notificationStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public PageResult<NotificationDTO> getAll(int page, int size) {
        Page<Notification> result = repository.findByIsDeleted(0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<NotificationDTO> dtoPage = result.map(notificationStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<NotificationDTO> findByUserId(Long userId,int page, int size) {
        Page<Notification> result = repository.findByUserIdAndIsDeleted(userId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<NotificationDTO> dtoPage = result.map(notificationStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<NotificationDTO> findByTenantId(Long tenantId,int page, int size) {
        Page<Notification> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<NotificationDTO> dtoPage = result.map(notificationStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<NotificationDTO> opt = repository.findById(id).map(notificationStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            NotificationDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(notificationStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
