package com.royalblood.happy6house.service.dto;

import com.royalblood.happy6house.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostUpdateDto {
    private String title;
    private String content;

    @Builder
    private PostUpdateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void apply(Post post) {
        post.update(title, content);
    }
}
