package com.flinksight.backend.service;

import com.flinksight.backend.domain.UserDepartment;
import com.flinksight.backend.mapper.UserApiStructMapper;
import com.flinksight.backend.mapper.UserDepartmentStructMapper;
import com.flinksight.backend.repository.UserDepartmentRepository;
import com.flinksight.common.dto.UserApiDTO;
import com.flinksight.common.dto.UserDepartmentDTO;
import com.flinksight.common.service.UserDepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDepartmentServiceImpl implements UserDepartmentService {
    private final UserDepartmentRepository repository;
    private final UserDepartmentStructMapper mapper;


    @Override
    public UserDepartmentDTO assignDepartmentToUser(Long userId, Long departmentId) {
        UserDepartment ud = UserDepartment.builder()
            .userId(userId)
            .departmentId(departmentId)
            .isDeleted(0)
            .build();
        return mapper.toDTO( repository.save(ud));
    }

    @Override
    public boolean removeDepartmentFromUser(Long userId, Long departmentId) {
        List<UserDepartment> list = repository.findByUserIdAndIsDeleted(userId, 0);
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
    public List<UserDepartmentDTO> findByUserId(Long userId) {
        return mapper.toDTOList(repository.findByUserIdAndIsDeleted(userId, 0));
    }

    @Override
    public List<UserDepartmentDTO> findByDepartmentId(Long departmentId) {
        return mapper.toDTOList(repository.findByDepartmentIdAndIsDeleted(departmentId, 0));
    }

    @Override
    public Optional<UserDepartmentDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<UserDepartmentDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserDepartmentDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
