package com.royalblood.happy6house.service;

import com.royalblood.happy6house.service.dto.PostCreateDto;

public interface PostService {
    // 글 작성
    Long post(PostCreateDto createDto);
}
