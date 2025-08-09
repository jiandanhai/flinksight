package com.flinksight.backend.service;

import com.flinksight.backend.domain.NotifyChannel;
import com.flinksight.backend.mapper.NotifyChannelMapper;
import com.flinksight.backend.repository.NotifyChannelRepository;
import com.flinksight.common.dto.NotifyChannelDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.NotifyChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    @Transactional
    public NotifyChannelDTO create(NotifyChannelDTO dto) {
        NotifyChannel entity = notifyChannelMapper.toEntity(dto);
        entity.setIsDeleted(0);
        NotifyChannel saved = repository.save(entity);
        return notifyChannelMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public NotifyChannelDTO update(NotifyChannelDTO dto) {
        NotifyChannel entity = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("渠道不存在"));
        NotifyChannel saved = repository.save(entity);
        return notifyChannelMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        NotifyChannel entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("渠道不存在"));
        entity.setIsDeleted(1);
        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public NotifyChannelDTO getById(Long id) {
        return repository.findById(id)
                .filter(e -> e.getIsDeleted() == 0)
                .map(notifyChannelMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("渠道不存在或已删除"));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<NotifyChannelDTO> listByTenant(Long tenantId,int page, int size) {
        Page<NotifyChannel> result = repository.findByTenantIdAndIsDeleted(tenantId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<NotifyChannelDTO> dtoPage = result.map(notifyChannelMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<NotifyChannelDTO> opt = repository.findById(id).map(notifyChannelMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            NotifyChannelDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(notifyChannelMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
