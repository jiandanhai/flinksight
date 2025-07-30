package com.flinksight.backend.service;

import com.flinksight.backend.domain.DataSource;
import com.flinksight.backend.domain.DeptRole;
import com.flinksight.backend.mapper.DataSourceStructMapper;
import com.flinksight.backend.mapper.DeptRoleStructMapper;
import com.flinksight.backend.repository.DeptRoleRepository;
import com.flinksight.common.dto.DataSourceDTO;
import com.flinksight.common.dto.DeptRoleDTO;
import com.flinksight.common.service.DeptRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeptRoleServiceImpl implements DeptRoleService {
    private final DeptRoleRepository repository;
    private final DeptRoleStructMapper mapper;

    @Override
    public DeptRoleDTO assignRoleToDept(Long deptId, Long roleId) {
        DeptRoleDTO dr = DeptRoleDTO.builder()
            .deptId(deptId)
            .roleId(roleId)
            .isDeleted(0)
            .build();
        DeptRole entity = mapper.toEntity(dr);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public boolean removeRoleFromDept(Long deptId, Long roleId) {
        List<DeptRoleDTO> list = mapper.toDTOList(repository.findByDeptIdAndIsDeleted(deptId,0));
        for (DeptRoleDTO dr : list) {
            if (dr.getRoleId().equals(roleId)) {
                dr.setIsDeleted(1);
                repository.save(mapper.toEntity(dr));
                return true;
            }
        }
        return false;
    }

    @Override
    public List<DeptRoleDTO> findByDeptId(Long deptId) {
        return mapper.toDTOList(repository.findByDeptIdAndIsDeleted(deptId,0));
    }

    @Override
    public List<DeptRoleDTO> findByRoleId(Long roleId) {
        return mapper.toDTOList(repository.findByRoleIdAndIsDeleted(roleId,0));
    }

    @Override
    public Optional<DeptRoleDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<DeptRoleDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            DeptRoleDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
