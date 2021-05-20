package com.royalblood.happy6house.repository.comment;

import com.royalblood.happy6house.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    List<Comment> findByUserId(Long userId);
    List<Comment> findByPostId(Long postId);
    List<Comment> findByParentId(Long parentId);
    void delete(Long id);
}
