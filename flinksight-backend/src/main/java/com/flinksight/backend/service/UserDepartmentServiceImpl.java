package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.UserDepartment;
import com.flinksight.backend.mapper.UserDepartmentStructMapper;
import com.flinksight.backend.repository.UserDepartmentRepository;
import com.flinksight.common.dto.UserDepartmentDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserDepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDepartmentServiceImpl implements UserDepartmentService {
    private final UserDepartmentRepository repository;
    private final UserDepartmentStructMapper userDepartmentStructMapper;


    @Override
    public UserDepartmentDTO assignDepartmentToUser(Long userId, Long departmentId) {
        UserDepartment ud = UserDepartment.builder()
            .userId(userId)
            .departmentId(departmentId)
            .isDeleted(0)
            .build();
        return userDepartmentStructMapper.toDTO( repository.save(ud));
    }

    @Override
    public boolean removeDepartmentFromUser(Long userId, Long departmentId) {
        List<UserDepartment> list = repository.findByUserIdAndDepartmentIdAndIsDeleted(userId, departmentId,0);
        for (UserDepartment ud : list) {
            if (ud.getDepartmentId().equals(departmentId)) {
                ud.setIsDeleted(1);
                repository.save(ud);
                return true;
            }
        }
        return false;
    }

    @Override
    public PageResult<UserDepartmentDTO> list(Long userId, Long departmentId, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, UserDepartment.class); // 统一 1→0
        Page<UserDepartment> result = repository.pageQuery(userId, departmentId, pr);
        return PageHelpers.toPageResult(result, userDepartmentStructMapper::toDTO, true); //
    }

    @Override
    public Optional<UserDepartmentDTO> getById(Long id) {
        return repository.findById(id).map(userDepartmentStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<UserDepartmentDTO> opt = repository.findById(id).map(userDepartmentStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserDepartmentDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(userDepartmentStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
