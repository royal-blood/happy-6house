package com.royalblood.happy6house.service;

import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.PostCategory;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.repository.post.PostRepository;
import com.royalblood.happy6house.service.dto.PostCreateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.royalblood.happy6house.domain.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    @Mock
    PostRepository postRepository;
    @InjectMocks
    PostServiceImpl postService;

    @DisplayName("게시글을 작성하고 생성된 게시글의 id를 반환하면 성공")
    @Test
    void 게시글을_작성하면_생성된_게시글의_id를_반환한다() {
        // given
        final User user = User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").auth(ROLE_USER.getText())
                .build();
        user.setId(10L);

        PostCreateDto createDto = PostCreateDto.builder()
                .title("This is title")
                .content("This is content")
                .category(PostCategory.GENERAL)
                .user(user)
                .build();

        Post createdPost = createDto.toEntity();
        createdPost.setId(1L);

        when(postRepository.save(any(Post.class))).thenReturn(createdPost);

        // when
        Long saveId = postService.post(createDto);

        // then
        assertThat(saveId).isSameAs(createdPost.getId());
    }
}