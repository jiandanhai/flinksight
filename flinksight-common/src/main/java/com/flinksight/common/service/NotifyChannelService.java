package com.flinksight.common.service;

import com.flinksight.common.dto.NotifyChannelDTO;
import com.flinksight.common.model.PageResult;

/**
 * 通知渠道业务接口
 */
public interface NotifyChannelService extends SoftDeleteService<NotifyChannelDTO, Long>{
    NotifyChannelDTO create(NotifyChannelDTO dto);
    NotifyChannelDTO update(NotifyChannelDTO dto);
    void delete(Long id);
    NotifyChannelDTO getById(Long id);
    PageResult<NotifyChannelDTO> listByTenant(Long tenantId,int page, int size);
}
