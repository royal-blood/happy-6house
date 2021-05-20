package com.royalblood.happy6house.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.royalblood.happy6house.domain.PostCategory;
import com.royalblood.happy6house.service.dto.PostCreateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateParam {
    private String title;
    private String content;
    private PostCategory category;

    @Builder
    PostCreateParam(String title, String content, PostCategory category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public PostCreateDto toDto() {
        return PostCreateDto.builder()
                .title(title)
                .content(content)
                .category(category)
                .build();
    }
}
