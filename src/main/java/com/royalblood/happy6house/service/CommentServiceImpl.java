package com.royalblood.happy6house.service;

import com.royalblood.happy6house.domain.Comment;
import com.royalblood.happy6house.exception.NotFoundException;
import com.royalblood.happy6house.repository.comment.CommentRepository;
import com.royalblood.happy6house.service.dto.CommentCreateDto;
import com.royalblood.happy6house.service.dto.CommentDto;
import com.royalblood.happy6house.service.dto.CommentUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    final private CommentRepository commentRepository;

    @Override
    @Transactional
    public Long create(CommentCreateDto createDto) {
        return commentRepository
                .save(createDto.toEntity())
                .getId();
    }

    @Override
    @Transactional
    public CommentDto update(Long userId, Long postId, Long commentId, CommentUpdateDto updateDto) {

        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(NotFoundException::new);

        if (!comment.getUser().getId().equals(userId))
            throw new RuntimeException("unauthorized");

        if (!comment.getPost().getId().equals(postId))
            throw new RuntimeException("bad request");

        updateDto.apply(comment);

        return CommentDto.of(comment);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long postId, Long commentId) {

        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(NotFoundException::new);

        if (!comment.getUser().getId().equals(userId))
            throw new RuntimeException("unauthorized");

        if (!comment.getPost().getId().equals(postId))
            throw new RuntimeException("bad request");

        commentRepository.delete(commentId);
    }

    @Override
    public CommentDto findById(Long id) {
        return commentRepository.findById(id)
                .map(CommentDto::of)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<CommentDto> findByUserId(Long userId) {
        return commentRepository.findByUserId(userId).stream()
                .map(CommentDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> findByParentId(Long parentId) {
        return commentRepository.findByParentId(parentId).stream()
                .map(CommentDto::of)
                .collect(Collectors.toList());
    }
}
