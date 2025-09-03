package com.flinksight.common.service;

import com.flinksight.common.dto.FlinkRestartFromLastRequestDTO;
import com.flinksight.common.dto.FlinkSavepointRequestDTO;
import com.flinksight.common.dto.FlinkSavepointResponseDTO;
import com.flinksight.common.dto.JobActionAckDTO;

public interface FlinkJobOpsService {
  FlinkSavepointResponseDTO triggerSavepoint(FlinkSavepointRequestDTO req);
  JobActionAckDTO restartFromLast(FlinkRestartFromLastRequestDTO req);
}