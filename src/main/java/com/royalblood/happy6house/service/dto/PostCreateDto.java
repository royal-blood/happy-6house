package com.royalblood.happy6house.service.dto;

import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.PostCategory;
import com.royalblood.happy6house.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCreateDto {
    private String title;
    private String content;
    private PostCategory category;
    private User user;

    @Builder
    private PostCreateDto(String title, String content,
                          PostCategory category, User user) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.user = user;
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .category(category)
                .user(user)
                .build();
    }

    public void setUser(User user) {
        this.user = user;
    }
}