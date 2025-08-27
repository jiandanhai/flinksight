package com.flinksight.common.service;

import com.flinksight.common.dto.NotifyChannelDTO;
import com.flinksight.common.model.PageResult;

import java.util.Optional;

/**
 * 通知渠道业务接口
 */
public interface NotifyChannelService extends SoftDeleteService<NotifyChannelDTO, Long>{
    NotifyChannelDTO create(NotifyChannelDTO dto);

    Optional<NotifyChannelDTO> getMyById(Long id);

    PageResult<NotifyChannelDTO> list(int page, int size);

    NotifyChannelDTO update(Long id, NotifyChannelDTO patch);

}
