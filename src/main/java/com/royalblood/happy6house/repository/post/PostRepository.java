package com.royalblood.happy6house.repository.post;

import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.PostCategory;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findByUserId(Long userId);
    List<Post> findByCategory(PostCategory category, Pageable pageable);
    List<Post> findAll();
    void deleteById(Long id);
}
