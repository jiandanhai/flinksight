package com.flinksight.common.service;

import com.flinksight.common.dto.JobRegisterRequestDTO;
import com.flinksight.common.dto.JobInfoDTO;

/**
 * 作业自动注册服务接口
 * 支持多租户、权限、链路追踪、幂等
 */
public interface JobRegisterService {

    /**
     * 作业自动注册
     * @param req 注册请求DTO
     * @return JobInfoDTO
     */
    JobInfoDTO register(JobRegisterRequestDTO req);

}
