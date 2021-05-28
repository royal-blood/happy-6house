package com.royalblood.happy6house.service.dto;

import com.royalblood.happy6house.domain.Comment;
import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.User;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateDto {

    private String content;
    private User user;
    private Post post;
    private Comment parent;

    @Builder
    private CommentCreateDto(String content, User user, Post post, Comment parent) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.parent = parent;
    }
    
    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .user(user)
                .post(post)
                .parent(parent)
                .build();
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setPost(Long postId) {
        this.post = Post.builder().build();
        this.post.setId(postId);
    }
    public void setParent(Long parentId) {
        this.parent = Comment.builder().build();
        this.parent.setId(parentId);
    }
}
