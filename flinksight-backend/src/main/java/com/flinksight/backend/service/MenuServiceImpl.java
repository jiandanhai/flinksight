package com.flinksight.backend.service;


import com.flinksight.backend.domain.Menu;
import com.flinksight.backend.mapper.MenuStructMapper;
import com.flinksight.backend.repository.MenuRepository;
import com.flinksight.backend.repository.PermissionRepository; // 若你把查询方法放这里就保留
import com.flinksight.backend.security.SecurityUtil;
import com.flinksight.common.dto.MenuNodeDTO;
import com.flinksight.common.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单服务（方案 A：menu.required_code 直接对齐 permission.code）
 *
 * 要求：
 * - Menu 实体包含字段 requiredCode(String) 对应列 menu.required_code
 * - PermissionRepository（或 PermissionQueryRepository）提供 findCodesByUser(userId, tenantId)
 * - MenuRepository 提供 findAllActiveOrderByOrderNumAsc()
 * - MenuStructMapper.toDTO(Menu, Locale) 负责中/英文标题选择与实体->DTO 映射
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepo;
    private final PermissionRepository permQueryRepo; // 或替换为你的 Query Repository
    private final MenuStructMapper menuStructMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MenuNodeDTO> getMenuTreeForUser(Locale locale) {
        // 1) 拉取用户权限码（一次 SQL，distinct）
        final Set<String> codes = new HashSet<>(permQueryRepo.findCodesByUser(SecurityUtil.getCurrentUserId(), SecurityUtil.getCurrentTenantId()));
        if (log.isDebugEnabled()) {
            log.debug("[menu] userId={}, tenantId={}, codes(size={}): {}",
                    SecurityUtil.getCurrentUserId(), SecurityUtil.getCurrentTenantId(), codes.size(), codes);
        }

        // 2) 拉取全部有效菜单（未删除，按 order_num,id 排序）
        final List<Menu> all = menuRepo.findAllActiveOrderByOrderNumAsc();
        if (all.isEmpty()) {
            log.warn("[menu] no active menus found!");
            return Collections.emptyList();
        }

        // 3) 过滤：requiredCode 为空 -> 放行；否则用户必须拥有该码
        final List<Menu> filtered = all.stream()
                .filter(m -> {
                    final String req = m.getRequiredCode();
                    return req == null || req.isBlank() || codes.contains(req);
                })
                .collect(Collectors.toList());

        // 4) 构建树（父不可见 -> 丢弃整棵子树；如需“提升为根”，切换策略见注释）
        final List<MenuNodeDTO> tree = buildTreeAsDto(filtered, locale, /*promoteOrphan=*/false);

        // 5) 兜底日志：前端空白时方便定位
        if (tree.isEmpty()) {
            log.warn("[menu] filtered tree is empty. userId={}, tenantId={}, userCodes={}",
                    SecurityUtil.getCurrentUserId(), SecurityUtil.getCurrentTenantId(), codes);
            // 打印最多 10 条菜单 requiredCode 统计，帮助排查“权限码对不齐”
            all.stream().limit(10).forEach(m ->
                    log.warn("  - menuKey={}, path={}, requiredCode={}",
                            m.getMenuKey(), m.getPath(), m.getRequiredCode()));
        }
        log.debug("##[menu tree] :{}",tree);
        return tree;
    }

    /**
     * 把扁平菜单变树并转 DTO
     * @param promoteOrphan true=父被过滤则“提升子为根”；false=父被过滤则“丢弃子树”
     */
    private List<MenuNodeDTO> buildTreeAsDto(List<Menu> allowed, Locale locale, boolean promoteOrphan) {
        // 先映射成 DTO，建立 id->dto 索引
        final Map<Long, MenuNodeDTO> idx = new LinkedHashMap<>();
        allowed.stream()
                .sorted(Comparator
                        .comparing(Menu::getOrderNum, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(Menu::getId))
                .forEach(m -> idx.put(m.getId(), menuStructMapper.toDTO(m, locale)));

        // 收集允许的 id 集合，加速父子判断
        final Set<Long> allowedIds = allowed.stream().map(Menu::getId).collect(Collectors.toSet());

        // 构造
        final List<MenuNodeDTO> roots = new ArrayList<>();
        for (Menu m : allowed) {
            final MenuNodeDTO dto = idx.get(m.getId());
            final Long pid = m.getParentId();

            if (pid == null) {
                roots.add(dto);
                continue;
            }

            final MenuNodeDTO p = idx.get(pid);
            if (p == null) {
                // 父节点不在 allowed（被过滤）
                if (promoteOrphan) {
                    roots.add(dto); // 策略一：提升为根
                } // 策略二：直接丢弃该节点（什么都不做）
                continue;
            }

            if (p.getChildren() == null) p.setChildren(new ArrayList<>());
            p.getChildren().add(dto);
        }

        // 对每个层级的 children 做一次排序，保证层级内稳定
        sortChildrenRecursive(roots);
        return roots;
    }

    private void sortChildrenRecursive(List<MenuNodeDTO> list) {
        if (list == null || list.isEmpty()) return;
        list.sort(Comparator
                .comparing(MenuNodeDTO::getOrderNum, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(MenuNodeDTO::getId, Comparator.nullsLast(Long::compareTo)));
        for (MenuNodeDTO n : list) {
            sortChildrenRecursive(n.getChildren());
        }
    }
}