package com.royalblood.happy6house.param;

import com.royalblood.happy6house.service.dto.CommentUpdateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateParam {

    private String content;

    @Builder
    CommentUpdateParam(String content) {
        this.content = content;
    }

    public CommentUpdateDto toDto() {
        return CommentUpdateDto.builder()
                .content(content)
                .build();
    }
}
