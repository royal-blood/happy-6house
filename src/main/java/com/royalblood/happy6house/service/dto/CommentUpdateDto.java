package com.royalblood.happy6house.service.dto;

import com.royalblood.happy6house.domain.Comment;
import lombok.Builder;

public class CommentUpdateDto {

    private String content;

    @Builder
    private CommentUpdateDto(String content) {
        this.content = content;
    }

    public void apply (Comment comment) {
        comment.update(content);
    }
}
