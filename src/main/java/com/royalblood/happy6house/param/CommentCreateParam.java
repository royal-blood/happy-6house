package com.royalblood.happy6house.param;

import com.royalblood.happy6house.domain.Comment;
import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.service.dto.CommentCreateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateParam {

    private String content;
    private Long parentId;

    @Builder
    public CommentCreateParam(String content) {
        this.content = content;
    }

    public CommentCreateDto toDto() {
        return CommentCreateDto.builder()
                .content(content)
                .build();
    }
}
