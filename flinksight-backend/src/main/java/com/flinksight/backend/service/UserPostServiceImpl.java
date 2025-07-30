package com.flinksight.backend.service;

import com.flinksight.backend.domain.UserPost;
import com.flinksight.backend.mapper.UserPermissionStructMapper;
import com.flinksight.backend.mapper.UserPostStructMapper;
import com.flinksight.backend.repository.UserPostRepository;
import com.flinksight.common.dto.UserPermissionDTO;
import com.flinksight.common.dto.UserPostDTO;
import com.flinksight.common.service.UserPostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPostServiceImpl implements UserPostService {
    private final UserPostRepository repository;
    private final UserPostStructMapper mapper;

    @Override
    public UserPostDTO assignPostToUser(Long userId, Long postId) {
        UserPost up = UserPost.builder()
            .userId(userId)
            .postId(postId)
            .isDeleted(0)
            .build();
        return mapper.toDTO( repository.save(up));
    }

    @Override
    public boolean removePostFromUser(Long userId, Long postId) {
        List<UserPost> list = repository.findByUserIdAndIsDeleted(userId, 0);
        for (UserPost up : list) {
            if (up.getPostId().equals(postId)) {
                up.setIsDeleted(1);
                repository.save(up);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<UserPostDTO> findByUserId(Long userId) {
        return mapper.toDTOList(repository.findByUserIdAndIsDeleted(userId, 0));
    }

    @Override
    public List<UserPostDTO> findByPostId(Long postId) {
        return mapper.toDTOList(repository.findByPostIdAndIsDeleted(postId, 0));
    }

    @Override
    public Optional<UserPostDTO> getById(Long id) {
        return repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean softDelete(Long id) {
        Optional<UserPostDTO> opt = repository.findById(id).map(mapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserPostDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(mapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
