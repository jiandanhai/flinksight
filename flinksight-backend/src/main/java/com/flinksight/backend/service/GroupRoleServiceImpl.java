package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.GroupRole;
import com.flinksight.backend.mapper.GroupRoleStructMapper;
import com.flinksight.backend.repository.GroupRoleRepository;
import com.flinksight.common.dto.GroupRoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.GroupRoleService;
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
public class GroupRoleServiceImpl implements GroupRoleService {
    private final GroupRoleRepository repository;
    private final GroupRoleStructMapper groupRoleStructMapper;

    @Override
    public GroupRoleDTO assignRoleToGroup(Long groupId, Long roleId) {
        GroupRoleDTO gr = GroupRoleDTO.builder()
            .groupId(groupId)
            .roleId(roleId)
            .isDeleted(0)
            .build();
        GroupRole entity = groupRoleStructMapper.toEntity(gr);
        return groupRoleStructMapper.toDTO(repository.save(entity));
    }

    @Override
    public boolean removeRoleFromGroup(Long groupId, Long roleId) {
        List<GroupRoleDTO> list = groupRoleStructMapper.toDTOList(repository.findByGroupIdAndRoleIdAndIsDeleted(groupId,roleId,0));
        for (GroupRoleDTO gr : list) {
            if (gr.getRoleId().equals(roleId)) {
                gr.setIsDeleted(1);
                repository.save(groupRoleStructMapper.toEntity(gr));
                return true;
            }
        }
        return false;
    }

    @Override
    public PageResult<GroupRoleDTO> list(Long groupId,Long roleId,int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, GroupRole.class); // 统一 1→0
        Page<GroupRole> result = repository.pageQuery(groupId,roleId, pr);
        return PageHelpers.toPageResult(result, groupRoleStructMapper::toDTO, true); // 返回 1-b
    }

    @Override
    public Optional<GroupRoleDTO> getById(Long id) {
        return repository.findById(id).map(groupRoleStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<GroupRoleDTO> opt = repository.findById(id).map(groupRoleStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            GroupRoleDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(groupRoleStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
