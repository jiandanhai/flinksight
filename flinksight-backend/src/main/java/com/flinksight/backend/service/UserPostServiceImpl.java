package com.flinksight.backend.service;

import com.flinksight.backend.common.PageHelpers;
import com.flinksight.backend.domain.UserPost;
import com.flinksight.backend.mapper.UserPostStructMapper;
import com.flinksight.backend.repository.UserPostRepository;
import com.flinksight.common.dto.UserPostDTO;
import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.UserPostService;
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
public class UserPostServiceImpl implements UserPostService {
    private final UserPostRepository repository;
    private final UserPostStructMapper userPostStructMapper;

    @Override
    public UserPostDTO assignPostToUser(Long userId, Long postId) {
        UserPost up = UserPost.builder()
            .userId(userId)
            .postId(postId)
            .isDeleted(0)
            .build();
        return userPostStructMapper.toDTO( repository.save(up));
    }

    @Override
    public boolean removePostFromUser(Long userId, Long postId) {
        List<UserPost> list = repository.findByUserIdAndPostIdAndIsDeleted(userId, postId,0);
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
    public PageResult<UserPostDTO> list(Long userId, Long postId, int page, int size) {
        PageRequest pr = PageHelpers.pageRequest(page, size, null, UserPost.class); // 统一 1→0
        Page<UserPost> result = repository.pageQuery(userId, postId, pr);
        return PageHelpers.toPageResult(result, userPostStructMapper::toDTO, true); //
    }


    @Override
    public Optional<UserPostDTO> getById(Long id) {
        return repository.findById(id).map(userPostStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
    }

    @Override
    public boolean sDelete(Long id) {
        Optional<UserPostDTO> opt = repository.findById(id).map(userPostStructMapper::toDTO).filter(e -> e.getIsDeleted() == 0);
        if (opt.isPresent()) {
            UserPostDTO dto = opt.get();
            dto.setIsDeleted(1);
            repository.save(userPostStructMapper.toEntity(dto));
            return true;
        }
        return false;
    }
}
