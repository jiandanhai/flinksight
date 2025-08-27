package com.flinksight.common.service;

import com.flinksight.common.dto.OrgNodeDTO;
import com.flinksight.common.model.PageResult;

import java.util.List;

public interface OrgNodeService extends SoftDeleteService<OrgNodeDTO, Long>{
    OrgNodeDTO create(OrgNodeDTO dto);
    OrgNodeDTO update(OrgNodeDTO dto);
    void delete(Long id);
    OrgNodeDTO getById(Long id);
    PageResult<OrgNodeDTO> list(int page, int size);
    List<OrgNodeDTO> getOrgTree();
}
