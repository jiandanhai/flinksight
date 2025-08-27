package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.OrgNode;
import com.flinksight.backend.mapper.OrgNodeMapper;
import com.flinksight.backend.repository.OrgNodeRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.OrgNodeDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.OrgNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrgNodeServiceImpl implements OrgNodeService {

    private final OrgNodeRepository repository;
    private final OrgNodeMapper orgNodeMapper;

    @Override
    @Transactional
    public OrgNodeDTO create(OrgNodeDTO dto) {
        OrgNode entity = orgNodeMapper.toEntity(dto);
        entity.setIsDeleted(0);
        OrgNode saved = repository.save(entity);
        return orgNodeMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public OrgNodeDTO update(OrgNodeDTO dto) {
        OrgNode entity = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("节点不存在"));
        entity.setIsDeleted(0);
        OrgNode saved = repository.save(entity);
        return orgNodeMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        OrgNode entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("节点不存在"));
        entity.setIsDeleted(1);
        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public OrgNodeDTO getById(Long id) {
        return repository.findById(id)
                .filter(e -> e.getIsDeleted() == 0)
                .map(orgNodeMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("节点不存在或已删除"));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<OrgNodeDTO> list(int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, OrgNode.class); // 统一 1→0
        Page<OrgNode> result = repository.findByTenantIdAndIsDeleted(SecurityUtil.getCurrentTenantId(),0, pr);
        return PageHelpers.toPageResult(result, orgNodeMapper::toDTO, true); // 返回
    }

    /**
     * 树形结构组装
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrgNodeDTO> getOrgTree() {
        // 1. 查flat list
        List<OrgNode> entities = repository.findAllByTenantId(SecurityUtil.getCurrentTenantId());
        List<OrgNodeDTO> flat = entities.stream().map(orgNodeMapper::toDTO).collect(Collectors.toList());

        // 2. 组装树结构
        Map<Long, OrgNodeDTO> idMap = flat.stream().collect(Collectors.toMap(OrgNodeDTO::getId, e -> e));
        List<OrgNodeDTO> roots = new ArrayList<>();
        for (OrgNodeDTO node : flat) {
            if (node.getParentId() == null || node.getParentId() == 0) {
                roots.add(node);
            } else {
                OrgNodeDTO parent = idMap.get(node.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) parent.setChildren(new ArrayList<>());
                    parent.getChildren().add(node);
                }
            }
        }
        return roots;
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<OrgNodeDTO> opt = repository.findById(id).map(orgNodeMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            OrgNodeDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(orgNodeMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
