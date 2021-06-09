package com.royalblood.happy6house.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.royalblood.happy6house.domain.Comment;
import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter @Setter
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String content;
    @JsonProperty("user")
    private UserDto userDto;
    @JsonProperty("post")
    private Long postId;
    @JsonProperty("parent")
    private Long parentId;

    @Builder
    private CommentDto(Long id, String content, UserDto userDto, Long postId, Long parentId) {
        this.id = id;
        this.content = content;
        this.userDto = userDto;
        this.postId = postId;
        this.parentId = parentId;
    }

    public static CommentDto of(Comment comment) {

        CommentDtoBuilder builder = builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userDto(UserDto.of(comment.getUser()))
                .postId(comment.getPost().getId());

        Optional.ofNullable(comment.getParent())
                .ifPresent(p->builder.parentId(p.getId()));

        return builder.build();
    }
}
