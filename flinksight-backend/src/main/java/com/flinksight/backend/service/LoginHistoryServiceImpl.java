package com.flinksight.backend.service;

import com.flinksight.backend.domain.Label;
import com.flinksight.backend.domain.LoginHistory;
import com.flinksight.backend.mapper.LabelStructMapper;
import com.flinksight.backend.mapper.LoginHistoryStructMapper;
import com.flinksight.backend.repository.LoginHistoryRepository;
import com.flinksight.common.dto.LabelDTO;
import com.flinksight.common.dto.LoginHistoryDTO;
import com.flinksight.common.service.LoginHistoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginHistoryServiceImpl implements LoginHistoryService {
    private final LoginHistoryRepository repository;
    private final LoginHistoryStructMapper mapper;

    @Override
    public LoginHistoryDTO create(LoginHistoryDTO loginHistoryDTO) {
        LoginHistory entity = mapper.toEntity(loginHistoryDTO);
        entity.setIsDeleted(0);
        LoginHistory saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public Optional<LoginHistoryDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public List<LoginHistoryDTO> findByUserId(Long userId) {
        return mapper.toDTOList(repository.findByUserIdAndIsDeleted(userId, 0));
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<LoginHistoryDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            LoginHistoryDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
