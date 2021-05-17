package com.royalblood.happy6house.service;

import com.royalblood.happy6house.domain.PostCategory;
import com.royalblood.happy6house.service.dto.PostCreateDto;
import com.royalblood.happy6house.service.dto.PostDto;

import java.util.List;

public interface PostService {
    // 글 작성
    Long post(PostCreateDto createDto);
    // id로 글 조회
    PostDto findById(Long userId);

    List<PostDto> findByCategory(PostCategory category, long offset, int limit);
}
