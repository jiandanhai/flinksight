package com.flinksight.common.service;

import com.flinksight.common.dto.JobInstanceDTO;

public interface JobAutoRecoverService {
    void checkAndRestart(JobInstanceDTO job);
}