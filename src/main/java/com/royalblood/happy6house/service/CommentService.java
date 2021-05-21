package com.royalblood.happy6house.service;

import com.royalblood.happy6house.service.dto.CommentCreateDto;
import com.royalblood.happy6house.service.dto.CommentDto;
import com.royalblood.happy6house.service.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {

    public Long create(CommentCreateDto createDto);

    public CommentDto update(Long userId, Long postId, Long commentId, CommentUpdateDto updateDto);

    public void delete(Long userId, Long postId, Long commentId);

    public CommentDto findById(Long id);

    public List<CommentDto> findByUserId(Long userId);

    public List<CommentDto> findByPostId(Long postId);

    public List<CommentDto> findByParentId(Long parentId);

}
