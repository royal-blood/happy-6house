package com.royalblood.happy6house.repository.post;

import com.royalblood.happy6house.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaPostRepository extends JpaRepository<Post, Long>, PostRepository {
    @Override
    Optional<Post> findById(@Param("id") Long id);

    @Override
    List<Post> findByUserId(Long userId);

    @Override
    List<Post> findAll();

    @Override
    void deleteById(Long id);
}
