package com.royalblood.happy6house.param;

import com.royalblood.happy6house.service.dto.PostUpdateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateParam {
    String title;
    String content;

    @Builder
    PostUpdateParam(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostUpdateDto toDto() {
        return PostUpdateDto.builder()
                .title(title)
                .content(content)
                .build();
    }
}
