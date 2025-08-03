package com.flinksight.backend.service;

import com.flinksight.backend.domain.UserGroup;
import com.flinksight.backend.mapper.UserGroupStructMapper;
import com.flinksight.backend.repository.UserGroupRepository;
import com.flinksight.common.dto.UserGroupDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserGroupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserGroupServiceImpl implements UserGroupService {
    private final UserGroupRepository repository;
    private final UserGroupStructMapper userGroupStructMapper;

    @Override
    public UserGroupDTO assignGroupToUser(Long userId, Long groupId) {
        UserGroup ug = UserGroup.builder()
            .userId(userId)
            .groupId(groupId)
            .isDeleted(0)
            .build();
        return userGroupStructMapper.toDTO( repository.save(ug));
    }

    @Override
    public boolean removeGroupFromUser(Long userId, Long groupId) {
        List<UserGroup> list = repository.findByUserIdAndGroupIdAndIsDeleted(userId, groupId,0);
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
    public PageResult<UserGroupDTO> findByUserId(Long userId,int page, int size) {
        Page<UserGroup> result = repository.findByUserIdAndIsDeleted(userId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<UserGroupDTO> dtoPage = result.map(userGroupStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public PageResult<UserGroupDTO> findByGroupId(Long groupId,int page, int size) {
        Page<UserGroup> result = repository.findByGroupIdAndIsDeleted(groupId,0, PageRequest.of(page, size, Sort.by("id").descending()));
        Page<UserGroupDTO> dtoPage = result.map(userGroupStructMapper::toDTO);
        return new PageResult<>(dtoPage);
    }

    @Override
    public Optional<UserGroupDTO> getById(Long id) {
        return repository.findById(id).map(userGroupStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<UserGroupDTO> opt = repository.findById(id).map(userGroupStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserGroupDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(userGroupStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
