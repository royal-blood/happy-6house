package com.royalblood.happy6house.repository.comment;

import com.royalblood.happy6house.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaCommentRepository extends JpaRepository<Comment, Long>, CommentRepository {

    @Override
    @Query("from Comment c where c.deleted = false and c.id = :id")
    Optional<Comment> findById(@Param("id") Long id);

    @Override
    List<Comment> findByUserId(@Param("userId") Long userId);

    @Override
    List<Comment> findByPostId(@Param("postId") Long postId);

    @Override
    List<Comment> findByParentId(@Param("parentId") Long parentId);

    @Transactional
    @Modifying
    @Override
    @Query(value = "update Comment c set c.deleted = true where c.id = :id")
    void delete(@Param("id") Long id);

}
