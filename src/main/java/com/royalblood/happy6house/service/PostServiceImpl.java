package com.royalblood.happy6house.service;

import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.PostCategory;
import com.royalblood.happy6house.exception.NotFoundException;
import com.royalblood.happy6house.repository.post.PostRepository;
import com.royalblood.happy6house.service.dto.PostCreateDto;
import com.royalblood.happy6house.service.dto.PostDto;
import com.royalblood.happy6house.service.dto.PostUpdateDto;
import com.royalblood.happy6house.utils.OffsetBasedPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    final private PostRepository postRepository;

    @Override
    public Long post(PostCreateDto createDto) {
        return postRepository.save(createDto.toEntity())
                .getId();
    }

    @Override
    public PostDto findById(Long id) {
        return postRepository.findById(id)
                .map(PostDto::of)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<PostDto> findByCategory(PostCategory category, long offset, int limit) {
        if (limit == 0) return new ArrayList<>();

        Pageable pageable = new OffsetBasedPageRequest(offset, limit,
                Sort.by(Sort.Direction.DESC, "createdDate"));

        return postRepository.findByCategory(category, pageable).stream()
                .map(PostDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public PostDto update(Long userId, Long postId, PostUpdateDto updateDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(NotFoundException::new);

        if (!post.getUser().getId().equals(userId))
            throw new RuntimeException("unauthorized");

        if (post.getCategory() != PostCategory.GUESTBOOK && updateDto.getTitle() == null)
            throw new RuntimeException("bad request");

        updateDto.apply(post);

        return PostDto.of(post);
    }

    @Transactional
    @Override
    public void deleteById(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(NotFoundException::new);

        if (!post.getUser().getId().equals(userId))
            throw new RuntimeException("unauthorized");

        postRepository.deleteById(postId);
    }
}
