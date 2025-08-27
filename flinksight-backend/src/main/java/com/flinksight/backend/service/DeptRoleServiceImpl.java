package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.DeptRole;
import com.flinksight.backend.mapper.DeptRoleStructMapper;
import com.flinksight.backend.repository.DeptRoleRepository;
import com.flinksight.common.dto.DeptRoleDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DeptRoleService;
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
public class DeptRoleServiceImpl implements DeptRoleService {
    private final DeptRoleRepository repository;
    private final DeptRoleStructMapper deptRoleStructMapper;

    @Override
    public DeptRoleDTO assignRoleToDept(Long deptId, Long roleId) {
        DeptRoleDTO dr = DeptRoleDTO.builder()
            .deptId(deptId)
            .roleId(roleId)
            .isDeleted(0)
            .build();
        DeptRole entity = deptRoleStructMapper.toEntity(dr);
        return deptRoleStructMapper.toDTO(repository.save(entity));
    }

    @Override
    public boolean removeRoleFromDept(Long deptId, Long roleId) {
        List<DeptRoleDTO> list = deptRoleStructMapper.toDTOList(repository.findByDeptIdAndRoleIdAndIsDeleted(deptId,roleId,0));
        for (DeptRoleDTO dr : list) {
            if (dr.getRoleId().equals(roleId)) {
                dr.setIsDeleted(1);
                repository.save(deptRoleStructMapper.toEntity(dr));
                return true;
            }
        }
        return false;
    }

    @Override
    public PageResult<DeptRoleDTO> list(Long deptId,Long roleId,int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, DeptRole.class); // 统一 1→0
        Page<DeptRole> result = repository.pageQuery(deptId, roleId, pr);
        return PageHelpers.toPageResult(result, deptRoleStructMapper::toDTO, true); // 返回 1-ba
    }

    @Override
    public Optional<DeptRoleDTO> getById(Long id) {
        return repository.findById(id).map(deptRoleStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<DeptRoleDTO> opt = repository.findById(id).map(deptRoleStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            DeptRoleDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(deptRoleStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
