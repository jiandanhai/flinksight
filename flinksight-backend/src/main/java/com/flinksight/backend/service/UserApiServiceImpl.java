package com.flinksight.backend.service;

import com.flinksight.backend.domain.Ticket;
import com.flinksight.backend.domain.UserApi;
import com.flinksight.backend.mapper.TicketStructMapper;
import com.flinksight.backend.mapper.UserApiStructMapper;
import com.flinksight.backend.repository.UserApiRepository;
import com.flinksight.common.dto.TicketDTO;
import com.flinksight.common.dto.UserApiDTO;
import com.flinksight.common.service.UserApiService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserApiServiceImpl implements UserApiService {
    private final UserApiRepository repository;
    private final UserApiStructMapper mapper;

    @Override
    public UserApiDTO assignApiToUser(Long userId, Long apiId) {
        UserApi ua = UserApi.builder()
            .userId(userId)
            .apiId(apiId)
            .isDeleted(0)
            .build();
        return mapper.toDTO( repository.save(ua));
    }

    @Override
    public boolean removeApiFromUser(Long userId, Long apiId) {
        List<UserApi> list = repository.findByUserIdAndIsDeleted(userId, 0);
        for (UserApi ua : list) {
            if (ua.getApiId().equals(apiId)) {
                ua.setIsDeleted(1);
                repository.save(ua);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<UserApiDTO> findByUserId(Long userId) {
        return mapper.toDTOList(repository.findByUserIdAndIsDeleted(userId, 0));
    }

    @Override
    public List<UserApiDTO> findByApiId(Long apiId) {
        return mapper.toDTOList(repository.findByApiIdAndIsDeleted(apiId, 0));
    }

    @Override
    public Optional<UserApiDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<UserApiDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserApiDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
