package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.OperationTemplate;
import com.flinksight.backend.domain.OpsTask;
import com.flinksight.backend.exception.BusinessException;
import com.flinksight.backend.mapper.OpsTaskMapper;
import com.flinksight.backend.repository.OperationTemplateRepository;
import com.flinksight.backend.repository.OpsTaskRepository;
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.FunnelResponseDTO;
import com.flinksight.common.dto.KpiResponseDTO;
import com.flinksight.common.dto.OpsTaskDTO;
import com.flinksight.common.enums.OpsTaskStatus;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.OpsTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;

/**
 * 运维自动化任务业务实现类
 * 负责运维任务的核心业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpsTaskServiceImpl implements OpsTaskService {

    private final OpsTaskRepository opsTaskRepository;
    private final OperationTemplateRepository tplRepo;
    private final OpsTaskMapper opsTaskMapper;


    @Override
    @Transactional
    public OpsTaskDTO create(OpsTaskDTO dto) {
        OpsTask e = opsTaskMapper.toEntity(dto);
        e.setIsDeleted(0);
        e.setStatus(OpsTaskStatus.PENDING.getCode());
        // 可选：基于模板初始化（复制模板元信息到任务描述）
        if (dto.getTemplateId() != null) {
            OperationTemplate tpl = tplRepo.findByIdAndTenantId(dto.getTemplateId(), SecurityUtil.getCurrentTenantId())
                    .orElseThrow(() -> BusinessException.notFound("模板不存在或无权限"));
            if (e.getDescription() == null || e.getDescription().isBlank()) {
                e.setDescription("From template: " + tpl.getType() + "/" + tpl.getName());
            }
            if (e.getType() == null) {
                e.setType(tpl.getType());
            }
            if (e.getName() == null) {
                e.setName(tpl.getName());
            }
        }
        try {
            OpsTask saved = opsTaskRepository.save(e);
            log.info("[task] created. id={}, tenant={}, name={}", saved.getId(), SecurityUtil.getCurrentTenantId(), saved.getName());
            return opsTaskMapper.toDTO(saved);
        } catch (DataIntegrityViolationException ex) {
            throw BusinessException.conflict("任务创建失败（数据约束冲突）");
        }
    }

    @Override
    @Transactional
    public OpsTaskDTO update(OpsTaskDTO dto) {
        OpsTask entity = opsTaskRepository.findById(dto.getId())
            .orElseThrow(() -> new IllegalArgumentException("任务不存在"));
        OpsTask saved = opsTaskRepository.save(entity);
        return opsTaskMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OpsTaskDTO getById(Long id) {
        return opsTaskRepository.findById(id)
            .filter(e -> e.getIsDeleted() == 0)
            .map(opsTaskMapper::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在或已删除"));
    }

    @Override
    public PageResult<OpsTaskDTO> list(String status, String keyword, String templateType, int page, int size) {
        // 统一 1→0，排序走 @DefaultSort 或全局默认
        PageRequest pr = PageHelpers.pageRequest(page, size, null, OpsTask.class);
        String st = StringUtils.hasText(status) ? status.trim() : "ALL";      // A 方案：仍要求 status，但允许 ALL
        String kw = StringUtils.hasText(keyword) ? keyword.trim() : null;
        String tp = StringUtils.hasText(templateType) ? templateType.trim() : null;
        var pageData = opsTaskRepository.pageQuery(
                SecurityUtil.getCurrentTenantId(), st, tp, kw, pr
        );

        // 直接返回 DTO，不需要 mapper
        return PageHelpers.toPageResult(pageData, Function.identity(), true);
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<OpsTaskDTO> opt = opsTaskRepository.findById(id).map(opsTaskMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            OpsTaskDTO dto = opt.get();
            dto.setIsDeleted(1);
            opsTaskRepository.save(opsTaskMapper.toEntity(dto));
            return true;
        }
        return false;
    }


    @Override
    public KpiResponseDTO getKpi(LocalDate from, LocalDate to) {
        LocalDate end = (to == null) ? LocalDate.now() : to;
        LocalDate start = (from == null) ? end.minusDays(13) : from;

        LocalDateTime fromTs = start.atStartOfDay();
        LocalDateTime toTs = end.atTime(LocalTime.MAX);

        // 创建数
        Map<LocalDate, Integer> createdMap = new HashMap<>();
        for (Object[] row : opsTaskRepository.countCreatedByDay(SecurityUtil.getCurrentUserId(), fromTs, toTs)) {
            LocalDate d = (LocalDate) row[0];
            Number n = (Number) row[1];
            createdMap.put(d, n == null ? 0 : n.intValue());
        }

        // 成功数
        Map<LocalDate, Integer> successMap = new HashMap<>();
        for (Object[] row : opsTaskRepository.countSuccessByDay(SecurityUtil.getCurrentUserId(), fromTs, toTs)) {
            LocalDate d = (LocalDate) row[0];
            Number n = (Number) row[1];
            successMap.put(d, n == null ? 0 : n.intValue());
        }

        // 按天补齐
        List<String> date = new ArrayList<>();
        List<Integer> active = new ArrayList<>();
        List<Integer> paid = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            date.add(d.toString());
            active.add(createdMap.getOrDefault(d, 0));
            paid.add(successMap.getOrDefault(d, 0));
        }

        return KpiResponseDTO.builder()
                .date(date)
                .active(active)
                .paid(paid)
                .build();
    }

    @Override
    public FunnelResponseDTO getFunnel(LocalDate dateOrNull) {
        LocalDate target = dateOrNull;
        if (target == null) {
            target = opsTaskRepository.findLatestBizDate(SecurityUtil.getCurrentTenantId()).orElse(null);
            if (target == null) {
                return FunnelResponseDTO.builder()
                        .steps(List.of())
                        .values(List.of())
                        .build();
            }
        }

        // 统计该日各状态数量
        Map<String, Integer> cnt = new HashMap<>();
        for (Object[] row : opsTaskRepository.countStatusOnDate(SecurityUtil.getCurrentTenantId(), target)) {
            String status = (String) row[0];
            Number n = (Number) row[1];
            cnt.put(status, n == null ? 0 : n.intValue());
        }

        // 统一漏斗顺序（如有其他状态可自行扩展）
        List<String> steps = List.of("已创建", "运行中", "成功", "失败");
        List<Integer> values = List.of(
                cnt.getOrDefault("PENDING", 0),   // 已创建/待执行
                cnt.getOrDefault("RUNNING", 0),
                cnt.getOrDefault("SUCCESS", 0),
                cnt.getOrDefault("FAILED", 0)
        );

        return FunnelResponseDTO.builder().steps(steps).values(values).build();
    }

    @Transactional
    @Override
    public OpsTaskDTO start(Long id) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        OpsTask e = opsTaskRepository.findByIdAndTenantIdAndIsDeleted(id, tenantId, 0)
                .orElseThrow(() -> BusinessException.notFound("任务不存在或已删除"));
        if (!OpsTaskStatus.PENDING.getCode().equals(e.getStatus())) {
            throw BusinessException.badRequest("仅 PENDING 任务可开始执行");
        }
        e.setStatus(OpsTaskStatus.RUNNING.name());
        e.setExecutedAt(LocalDateTime.now());
        OpsTask saved = opsTaskRepository.save(e);
        log.info("[task] started. id={}, tenant={}", id, tenantId);
        return opsTaskMapper.toDTO(saved);
    }

    @Transactional
    @Override
    public OpsTaskDTO complete(OpsTaskDTO req) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        OpsTask e = opsTaskRepository.findByIdAndTenantIdAndIsDeleted(req.getId(), tenantId, 0)
                .orElseThrow(() -> BusinessException.notFound("任务不存在或已删除"));
        if (!OpsTaskStatus.RUNNING.getCode().equals(e.getStatus())) {
            throw BusinessException.badRequest("仅 RUNNING 任务可标记完成");
        }
        e.setStatus(OpsTaskStatus.SUCCESS.getCode());
        if (req.getDescription() != null && !req.getDescription().isBlank()) {
            e.setDescription(appendMsg(e.getDescription(), req.getDescription()));
        }
        OpsTask saved = opsTaskRepository.save(e);
        log.info("[task] completed. id={}, tenant={}", req.getId(), tenantId);
        return opsTaskMapper.toDTO(saved);
    }

    @Transactional
    @Override
    public OpsTaskDTO fail(OpsTaskDTO req) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        OpsTask e = opsTaskRepository.findByIdAndTenantIdAndIsDeleted(req.getId(), tenantId, 0)
                .orElseThrow(() -> BusinessException.notFound("任务不存在或已删除"));
        if (!OpsTaskStatus.RUNNING.getCode().equals(e.getStatus())) {
            throw BusinessException.badRequest("仅 RUNNING 任务可标记失败");
        }
        e.setStatus(OpsTaskStatus.FAILED.getCode());
        if (req.getDescription() != null && !req.getDescription().isBlank()) {
            e.setDescription(appendMsg(e.getDescription(), req.getDescription()));
        }
        OpsTask saved = opsTaskRepository.save(e);
        log.info("[task] failed. id={}, tenant={}", req.getId(), tenantId);
        return opsTaskMapper.toDTO(saved);
    }

    @Transactional
    @Override
    public OpsTaskDTO cancel(OpsTaskDTO req) {
        Long tenantId = SecurityUtil.getCurrentTenantId();
        OpsTask e = opsTaskRepository.findByIdAndTenantIdAndIsDeleted(req.getId(), tenantId, 0)
                .orElseThrow(() -> BusinessException.notFound("任务不存在或已删除"));
        if (OpsTaskStatus.SUCCESS.getCode().equals(e.getStatus())) {
            throw BusinessException.badRequest("成功任务不可取消");
        }
        e.setStatus(OpsTaskStatus.CANCELLED.getCode());
        if (req.getDescription() != null && !req.getDescription().isBlank()) {
            e.setDescription(appendMsg(e.getDescription(), req.getDescription()));
        }
        OpsTask saved = opsTaskRepository.save(e);
        log.info("[task] cancelled. id={}, tenant={}", req.getId(), tenantId);
        return opsTaskMapper.toDTO(saved);
    }

    private static String appendMsg(String origin, String msg) {
        if (origin == null || origin.isBlank()) return msg;
        return origin + " | " + msg;
    }
}
