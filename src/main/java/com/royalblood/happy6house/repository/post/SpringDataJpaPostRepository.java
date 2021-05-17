package com.royalblood.happy6house.repository.post;

import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.PostCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaPostRepository extends JpaRepository<Post, Long>, PostRepository {
    @Override
    @Query("from Post p where p.id = :id and p.deleted = false")
    Optional<Post> findById(@Param("id") Long id);

    @Override
    List<Post> findByUserId(Long userId);

    @Override
    @Query("from Post p where p.category = :category and p.deleted = false")
    List<Post> findByCategory(@Param("category") PostCategory category,
                              Pageable pageable);

    @Override
    List<Post> findAll();

    @Transactional
    @Modifying
    @Override
    @Query("update Post p set p.deleted = true where p.id = :id")
    void deleteById(Long id);



}
