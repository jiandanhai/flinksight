package com.flinksight.backend.service;

import com.flinksight.backend.domain.RoleMenu;
import com.flinksight.backend.mapper.RoleDataScopeStructMapper;
import com.flinksight.backend.mapper.RoleMenuStructMapper;
import com.flinksight.backend.repository.RoleMenuRepository;
import com.flinksight.common.dto.RoleDataScopeDTO;
import com.flinksight.common.dto.RoleMenuDTO;
import com.flinksight.common.service.RoleMenuService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleMenuServiceImpl implements RoleMenuService {
    private final RoleMenuRepository repository;
    private final RoleMenuStructMapper mapper;

    @Override
    public RoleMenuDTO assignMenuToRole(Long roleId, Long menuId) {
        RoleMenu rm = RoleMenu.builder()
            .roleId(roleId)
            .menuId(menuId)
            .isDeleted(0)
            .build();
        return mapper.toDTO(repository.save(rm));
    }

    @Override
    public boolean removeMenuFromRole(Long roleId, Long menuId) {
        List<RoleMenu> list = repository.findByRoleIdAndIsDeleted(roleId, 0);
        for (RoleMenu rm : list) {
            if (rm.getMenuId().equals(menuId)) {
                rm.setIsDeleted(1);
                repository.save(rm);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<RoleMenuDTO> findByRoleId(Long roleId) {
        return mapper.toDTOList(repository.findByRoleIdAndIsDeleted(roleId, 0));
    }

    @Override
    public List<RoleMenuDTO> findByMenuId(Long menuId) {
        return mapper.toDTOList(repository.findByMenuIdAndIsDeleted(menuId, 0));
    }

    @Override
    public Optional<RoleMenuDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<RoleMenuDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            RoleMenuDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
