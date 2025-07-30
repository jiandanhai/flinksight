package com.flinksight.backend.service;

import com.flinksight.backend.domain.DeptRole;
import com.flinksight.backend.domain.GroupRole;
import com.flinksight.backend.mapper.DeptRoleStructMapper;
import com.flinksight.backend.mapper.GroupRoleStructMapper;
import com.flinksight.backend.repository.GroupRoleRepository;
import com.flinksight.common.dto.DeptRoleDTO;
import com.flinksight.common.dto.GroupRoleDTO;
import com.flinksight.common.service.GroupRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupRoleServiceImpl implements GroupRoleService {
    private final GroupRoleRepository repository;
    private final GroupRoleStructMapper mapper;

    @Override
    public GroupRoleDTO assignRoleToGroup(Long groupId, Long roleId) {
        GroupRoleDTO gr = GroupRoleDTO.builder()
            .groupId(groupId)
            .roleId(roleId)
            .isDeleted(0)
            .build();
        GroupRole entity = mapper.toEntity(gr);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public boolean removeRoleFromGroup(Long groupId, Long roleId) {
        List<GroupRoleDTO> list = mapper.toDTOList(repository.findByGroupIdAndIsDeleted(groupId,0));
        for (GroupRoleDTO gr : list) {
            if (gr.getRoleId().equals(roleId)) {
                gr.setIsDeleted(1);
                repository.save(mapper.toEntity(gr));
                return true;
            }
        }
        return false;
    }

    @Override
    public List<GroupRoleDTO> findByGroupId(Long groupId) {
        return mapper.toDTOList(repository.findByGroupIdAndIsDeleted(groupId,0));
    }

    @Override
    public List<GroupRoleDTO> findByRoleId(Long roleId) {
        return mapper.toDTOList(repository.findByRoleIdAndIsDeleted(roleId,0));
    }

    @Override
    public Optional<GroupRoleDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<GroupRoleDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            GroupRoleDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
