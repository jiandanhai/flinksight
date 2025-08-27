package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.UserApi;
import com.flinksight.backend.mapper.UserApiStructMapper;
import com.flinksight.backend.repository.UserApiRepository;
import com.flinksight.common.dto.UserApiDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserApiService;
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
public class UserApiServiceImpl implements UserApiService {
    private final UserApiRepository repository;
    private final UserApiStructMapper userApiStructMapper;

    @Override
    public UserApiDTO assignApiToUser(Long userId, Long apiId) {
        UserApi ua = UserApi.builder()
            .userId(userId)
            .apiId(apiId)
            .isDeleted(0)
            .build();
        return userApiStructMapper.toDTO( repository.save(ua));
    }

    @Override
    public boolean removeApiFromUser(Long userId, Long apiId) {
        List<UserApi> list = repository.findByUserIdAndApiIdAndIsDeleted(userId, apiId,0);
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
    public PageResult<UserApiDTO> list(Long userId, Long apiId, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, UserApi.class); // 统一 1→0
        Page<UserApi> result = repository.pageQuery(userId, apiId, pr);
        return PageHelpers.toPageResult(result, userApiStructMapper::toDTO, true); //
    }

    @Override
    public Optional<UserApiDTO> getById(Long id) {
        return repository.findById(id).map(userApiStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<UserApiDTO> opt = repository.findById(id).map(userApiStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserApiDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(userApiStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
