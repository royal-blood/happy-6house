package com.royalblood.happy6house.repository.post;

import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findByUserId(Long userId);
    List<Post> findAll();
//    List<Post> find(int limit, Type, start);
//    void delete(Long id);
}
