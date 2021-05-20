package com.royalblood.happy6house.service;

import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.PostCategory;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.repository.post.PostRepository;
import com.royalblood.happy6house.service.dto.PostCreateDto;
import com.royalblood.happy6house.service.dto.PostDto;
import com.royalblood.happy6house.service.dto.PostUpdateDto;
import com.royalblood.happy6house.service.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.royalblood.happy6house.domain.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;

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

    @DisplayName("id에 해당하는 게시글 DTO를 반환하면 성공")
    @Test
    void id로_게시글_조회() {
        // given
        final Post post = Post.builder()
                .title("This is Title")
                .content("This is content")
                .category(PostCategory.GENERAL)
                .user(mock(User.class))
                .build();

        Long postId = 10L;
        ReflectionTestUtils.setField(post, "id", postId);

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // when
        PostDto postDto = postService.findById(postId);

        // then
        verify(postRepository).findById(any(Long.class));
        assertThat(postId).isSameAs(postDto.getId());
    }

    @DisplayName("general category에 해당하고 주어진 limit과 offset에 의해 올바른 수의 게시글 DTO list를 반환하면 성공")
    @Test
    void general_category로_게시글_조회() {
        // given
        int totalGeneralPostsSize = 10;
        List<Post> posts = new ArrayList<>();
        for (long i = 1; i <= totalGeneralPostsSize; i++) {
            Post post = Post.builder()
                    .title("title")
                    .content("content")
                    .category(PostCategory.GENERAL)
                    .user(mock(User.class))
                    .build();
            ReflectionTestUtils.setField(post, "id", i);
            ReflectionTestUtils.setField(post, "createdDate", LocalDateTime.now());
            ReflectionTestUtils.setField(post, "modifiedDate", LocalDateTime.now());
            posts.add(post);
        }
        long offset = 0;
        int limit = 10;

//        Collections.sort(posts, (a, b)-> b.getCreatedDate().compareTo(a.getCreatedDate()));
        posts.sort(Comparator.comparing(Post::getCreatedDate).reversed());
        willReturn(posts).
                given(postRepository).findByCategory(any(PostCategory.class), any(Pageable.class));

        // when
        List<PostDto> postDtoList = postService.findByCategory(PostCategory.GENERAL, offset, limit);

        // then
        verify(postRepository).findByCategory(any(PostCategory.class), any(Pageable.class));
        assertThat(postDtoList.size()).isSameAs(totalGeneralPostsSize);
    }

    @DisplayName("게시글이 수정되면 성공")
    @Test
    void 게시글_수정() {
        // given
        final User user = User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").auth(ROLE_USER.getText())
                .build();
        Long userId = 20L;
        user.setId(userId);

        final Post post = Post.builder()
                .title("This is Title")
                .content("This is content")
                .category(PostCategory.GENERAL)
                .user(user)
                .build();
        Long postId = 10L;
        ReflectionTestUtils.setField(post, "id", postId);

        String newTitle = "It's new Title";
        String newContent = "It's new content";
        PostUpdateDto updateDto = PostUpdateDto.builder()
                .title(newTitle).content(newContent)
                .build();

        willReturn(Optional.of(post)).given(postRepository).findById(postId);

        // when
        PostDto updatedPostDto = postService.update(userId, postId, updateDto);

        // then
        verify(postRepository).findById(any(Long.class));
        assertThat(updatedPostDto.getTitle()).isSameAs(newTitle);
        assertThat(updatedPostDto.getContent()).isSameAs(newContent);
    }

    @DisplayName("게시글 DB 삭제가 호출되면 성공")
    @Test
    void 게시글_삭제() {
        // given
        final User user = User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").auth(ROLE_USER.getText())
                .build();
        Long userId = 20L;
        user.setId(userId);

        final Post post = Post.builder()
                .title("This is Title")
                .content("This is content")
                .category(PostCategory.GENERAL)
                .user(user)
                .build();
        Long postId = 10L;
        ReflectionTestUtils.setField(post, "id", postId);

        willReturn(Optional.of(post)).given(postRepository).findById(postId);

        // when
        postService.deleteById(userId, postId);

        // then
        verify(postRepository).findById(any(Long.class));
        verify(postRepository).deleteById(any(Long.class));
    }
}