package com.flinksight.backend.service;

import com.flinksight.backend.domain.UserGroup;
import com.flinksight.backend.mapper.UserDepartmentStructMapper;
import com.flinksight.backend.mapper.UserGroupStructMapper;
import com.flinksight.backend.repository.UserGroupRepository;
import com.flinksight.common.dto.UserDepartmentDTO;
import com.flinksight.common.dto.UserGroupDTO;
import com.flinksight.common.service.UserGroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserGroupServiceImpl implements UserGroupService {
    private final UserGroupRepository repository;
    private final UserGroupStructMapper mapper;

    @Override
    public UserGroupDTO assignGroupToUser(Long userId, Long groupId) {
        UserGroup ug = UserGroup.builder()
            .userId(userId)
            .groupId(groupId)
            .isDeleted(0)
            .build();
        return mapper.toDTO( repository.save(ug));
    }

    @Override
    public boolean removeGroupFromUser(Long userId, Long groupId) {
        List<UserGroup> list = repository.findByUserIdAndIsDeleted(userId, 0);
        for (UserGroup ug : list) {
            if (ug.getGroupId().equals(groupId)) {
                ug.setIsDeleted(1);
                repository.save(ug);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<UserGroupDTO> findByUserId(Long userId) {
        return mapper.toDTOList(repository.findByUserIdAndIsDeleted(userId, 0));
    }

    @Override
    public List<UserGroupDTO> findByGroupId(Long groupId) {
        return mapper.toDTOList(repository.findByGroupIdAndIsDeleted(groupId, 0));
    }

    @Override
    public Optional<UserGroupDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<UserGroupDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserGroupDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
