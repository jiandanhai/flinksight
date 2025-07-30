package com.flinksight.backend.service;

import com.flinksight.backend.domain.Node;
import com.flinksight.backend.domain.Notification;
import com.flinksight.backend.mapper.NodeStructMapper;
import com.flinksight.backend.mapper.NotificationStructMapper;
import com.flinksight.backend.repository.NotificationRepository;
import com.flinksight.common.dto.NodeDTO;
import com.flinksight.common.dto.NodeHealthDTO;
import com.flinksight.common.dto.NotificationDTO;
import com.flinksight.common.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final NotificationStructMapper mapper;

    @Override
    public NotificationDTO createOrUpdate(NotificationDTO notificationDTO) {
        Notification entity = mapper.toEntity(notificationDTO);
        entity.setIsDeleted(0);
        Notification saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<NotificationDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<NotificationDTO> getAll() {
      return mapper.toDTOList(repository.findAll().stream().filter(e -> e.getIsDeleted() == 0).toList());
    }

    @Override
    public List<NotificationDTO> findByUserId(Long userId) {
        return mapper.toDTOList(repository.findByUserIdAndIsDeleted(userId, 0));
    }

    @Override
    public List<NotificationDTO> findByTenantId(Long tenantId) {
        return mapper.toDTOList(repository.findByTenantIdAndIsDeleted(tenantId, 0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<NotificationDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            NotificationDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
