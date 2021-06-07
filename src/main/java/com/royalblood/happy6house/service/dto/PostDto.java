package com.royalblood.happy6house.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.royalblood.happy6house.domain.Comment;
import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.PostCategory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private PostCategory category;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    @JsonProperty("user")
    private UserDto userDto;
    @JsonProperty("comment")
    private List<CommentDto> comments;

    @Builder
    private PostDto(Long id, String title, String content,
                    PostCategory category, UserDto userDto,
                    LocalDateTime createdDate, LocalDateTime modifiedDate,
                    List<CommentDto> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.userDto = userDto;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.comments = comments;
    }

    public static PostDto of(Post post) {

        return builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .userDto(UserDto.of(post.getUser()))
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .comments(post.getComments().stream()
                        .map(CommentDto::of)
                        .collect(Collectors.toList()))
                .build();
    }
}
