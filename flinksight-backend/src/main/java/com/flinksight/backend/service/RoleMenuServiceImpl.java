package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.RoleMenu;
import com.flinksight.backend.mapper.RoleMenuStructMapper;
import com.flinksight.backend.repository.RoleMenuRepository;
import com.flinksight.common.dto.RoleMenuDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.RoleMenuService;
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
public class RoleMenuServiceImpl implements RoleMenuService {
    private final RoleMenuRepository repository;
    private final RoleMenuStructMapper roleMenuStructMapper;

    @Override
    public RoleMenuDTO assignMenuToRole(Long roleId, Long menuId) {
        RoleMenu rm = RoleMenu.builder()
            .roleId(roleId)
            .menuId(menuId)
            .isDeleted(0)
            .build();
        return roleMenuStructMapper.toDTO(repository.save(rm));
    }

    @Override
    public boolean removeMenuFromRole(Long roleId, Long menuId) {
        List<RoleMenu> list = repository.findByRoleIdAndMenuIdAndIsDeleted(roleId, menuId,0);
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
    public PageResult<RoleMenuDTO> list(Long roleId, Long menuId, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, RoleMenu.class); // 统一 1→0
        Page<RoleMenu> result = repository.pageQuery(roleId, menuId, pr);
        return PageHelpers.toPageResult(result, roleMenuStructMapper::toDTO, true); // 返回
    }

    @Override
    public Optional<RoleMenuDTO> getById(Long id) {
        return repository.findById(id).map(roleMenuStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<RoleMenuDTO> opt = repository.findById(id).map(roleMenuStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            RoleMenuDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(roleMenuStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
