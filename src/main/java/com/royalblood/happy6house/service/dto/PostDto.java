package com.royalblood.happy6house.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.PostCategory;
import lombok.*;

import java.time.LocalDateTime;

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

    @Builder
    private PostDto(Long id, String title, String content,
                    PostCategory category, UserDto userDto,
                    LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.userDto = userDto;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
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
                .build();
    }
}
