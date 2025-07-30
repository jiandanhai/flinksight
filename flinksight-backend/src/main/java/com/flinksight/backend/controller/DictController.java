package com.flinksight.backend.controller;

import com.flinksight.backend.domain.Dict;
import com.flinksight.common.dto.DictDTO;
import com.flinksight.common.service.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * 数据字典管理
 */
@RestController
@RequestMapping("/api/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictService service;

    @PostMapping
    public DictDTO create(@RequestBody DictDTO dto) {
        return service.createOrUpdate(dto);
    }

    @GetMapping("/{id}")
    public Optional<DictDTO> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/type/{dictType}")
    public List<DictDTO> findByDictType(@PathVariable String dictType) {
        return service.findByDictType(dictType);
    }

    @PutMapping
    public DictDTO update(@RequestBody DictDTO dto) {
        return service.createOrUpdate(dto);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return service.softDelete(id);
    }
}
