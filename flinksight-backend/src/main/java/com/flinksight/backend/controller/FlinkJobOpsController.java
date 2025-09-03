package com.flinksight.backend.controller;


import com.flinksight.common.dto.FlinkRestartFromLastRequestDTO;
import com.flinksight.common.dto.FlinkSavepointRequestDTO;
import com.flinksight.common.dto.FlinkSavepointResponseDTO;
import com.flinksight.common.dto.JobActionAckDTO;
import com.flinksight.common.service.FlinkJobOpsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Flink 作业安全动作", description = "触发 Savepoint、从最近 Savepoint 重启（仅作业级联动）")
@RestController
@RequestMapping("/api/runtime/flink/jobs")
@RequiredArgsConstructor
public class FlinkJobOpsController {

  private final FlinkJobOpsService svc;

  @Operation(summary="触发 savepoint")
  @PostMapping("/savepoint")
  public ResponseEntity<FlinkSavepointResponseDTO> savepoint(@Valid @RequestBody FlinkSavepointRequestDTO req){
    return ResponseEntity.ok(svc.triggerSavepoint(req));
  }

  @Operation(summary="从最近 savepoint 重启（任务化）")
  @PostMapping("/restart-from-last")
  public ResponseEntity<JobActionAckDTO> restartFromLast(@Valid @RequestBody FlinkRestartFromLastRequestDTO req){
    return ResponseEntity.accepted().body(svc.restartFromLast(req));
  }
}