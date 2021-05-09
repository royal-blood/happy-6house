package com.royalblood.happy6house.service;

import com.royalblood.happy6house.repository.post.PostRepository;
import com.royalblood.happy6house.service.dto.PostCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    @Autowired private PostRepository postRepository;

    @Override
    public Long post(PostCreateDto createDto) {
        return postRepository.save(createDto.toEntity())
                .getId();
    }
}
