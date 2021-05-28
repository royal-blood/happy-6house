package com.royalblood.happy6house.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.royalblood.happy6house.domain.Comment;
import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.PostCategory;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.param.CommentCreateParam;
import com.royalblood.happy6house.param.CommentUpdateParam;
import com.royalblood.happy6house.security.jwt.JwtAuthenticationEntryPoint;
import com.royalblood.happy6house.security.jwt.JwtTokenUtil;
import com.royalblood.happy6house.service.CommentService;
import com.royalblood.happy6house.service.JwtUserDetailsService;
import com.royalblood.happy6house.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.royalblood.happy6house.domain.Role.ROLE_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @MockBean
    JwtUserDetailsService jwtUserDetailsService;
    @MockBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    JwtTokenUtil jwtTokenUtil;

    @MockBean
    CommentService commentService;

    @Autowired private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private User user;
    private Post post;

    @BeforeEach
    void init() {

        Long userId = 10L;
        user = User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").picture(null)
                .auth(ROLE_USER.getText())
                .build();
        ReflectionTestUtils.setField(user, "id", userId);

        Long postId = 100L;
        post = Post.builder()
                .user(user)
                .title("title")
                .content("content")
                .category(PostCategory.GENERAL)
                .build();
        ReflectionTestUtils.setField(post, "id", postId);

    }

    @DisplayName("댓글 생성 시 성공 메세지 리턴 확인")
    @Test
    void 댓글_생성_및_성공_메세지() throws Exception {

        // given
        Long commentId = 11L;
        CommentCreateParam createParam = CommentCreateParam.builder()
                .content("content")
                .build();

        given(commentService
                .create(any(CommentCreateDto.class))).willReturn(commentId);

        // when-then
        mockMvc.perform(
                post("/api/posts/100/comments").with(user(user))
                .content(mapper.writeValueAsString(createParam))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(commentId));

    }

    @DisplayName("id로 댓글 조회 시 성공")
    @Test
    void id로_댓글_조회() throws Exception {

        // given
        Long commentId = 11L;
        CommentDto commentDto = CommentDto.builder()
                .userDto(UserDto.of(user))
                .postDto(PostDto.of(post))
                .content("content")
                .build();

        ReflectionTestUtils.setField(commentDto, "id", commentId);

        given(commentService.findById(any(Long.class))).willReturn(commentDto);

        // when
        mockMvc.perform(
                get("/api/comments/{commentId}", commentId)
                .with(user(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.user.id").value(commentDto.getUserDto().getId()))
                .andExpect(jsonPath("$.post.id").value(commentDto.getPostDto().getId()));

        // then
        then(commentService)
                .should(times(1))
                .findById(any(Long.class));

    }

    @DisplayName("유저 id로 댓글 조회 시 성공")
    @Test
    void 유저_id로_댓글_조회() throws Exception {

        // given
        int totalCommentsSize = 5;

        List<CommentDto> comments = new ArrayList<>();

        for (int i=0; i<totalCommentsSize; i++) {

            CommentDto commentDto = CommentDto.builder()
                    .content("content")
                    .userDto(UserDto.of(user))
                    .postDto(PostDto.of(post))
                    .build();

            comments.add(commentDto);
        }

        given(commentService
                .findByUserId(any(Long.class))).willReturn(comments);

        // when
        mockMvc.perform(
                get("/api/users/{userId}/comments", user.getId())
                .with(user(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(totalCommentsSize));

        // then
        then(commentService)
                .should(times(1))
                .findByUserId(any(Long.class));
    }

    @DisplayName("게시글 id로 댓글 조회 시 성공")
    @Test
    void 댓글_id로_댓글_조회() throws Exception {

        // given
        int totalCommentsSize = 5;

        List<CommentDto> comments = new ArrayList<>();

        for (int i=0; i<totalCommentsSize; i++) {

            CommentDto commentDto = CommentDto.builder()
                    .content("content")
                    .userDto(UserDto.of(user))
                    .postDto(PostDto.of(post))
                    .build();

            comments.add(commentDto);
        }

        given(commentService
                .findByPostId(post.getId())).willReturn(comments);

        // when
        mockMvc.perform(
                get("/api/posts/{userId}/comments", post.getId())
                .with(user(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(totalCommentsSize));

        // then
        then(commentService)
                .should(times(1))
                .findByPostId(any(Long.class));
    }

    @DisplayName("댓글 id로 대댓글 조회 시 성공")
    @Test
    void 댓글_id로_대댓글_조회() throws Exception {

        // given
        Long parentId = 11L;
        Comment parent = Comment.builder()
                .content("content")
                .user(user)
                .post(post)
                .build();
        ReflectionTestUtils.setField(parent, "id", parentId);

        int totalCommentsSize = 5;

        List<CommentDto> comments = new ArrayList<>();

        for (int i=0; i<totalCommentsSize; i++) {

            CommentDto commentDto = CommentDto.builder()
                    .content("content")
                    .userDto(UserDto.of(user))
                    .postDto(PostDto.of(post))
                    .parentId(parentId)
                    .build();

            comments.add(commentDto);
        }

        given(commentService
                .findByParentId(parentId)).willReturn(comments);

        // when
        mockMvc.perform(
                get("/api/posts/{postId}/comments", post.getId())
                .queryParam("parentId", Long.toString(parentId))
                .with(user(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(totalCommentsSize));

        // then
        then(commentService)
                .should(times(1))
                .findByParentId(any(Long.class));

    }

    @DisplayName("댓글_수정_시_성공")
    @Test
    void 댓글_수정() throws Exception {

        // given
        Long commentId = 11L;
        CommentUpdateParam updateParam = CommentUpdateParam.builder()
                .content("update")
                .build();

        CommentDto commentDto = CommentDto.builder()
                .userDto(UserDto.of(user))
                .postDto(PostDto.of(post))
                .id(commentId)
                .content("update")
                .build();

        given(commentService
                .update(any(Long.class),any(Long.class),any(Long.class),any(CommentUpdateDto.class)))
                .willReturn(commentDto);

        // when-then
        mockMvc.perform(
                put("/api/posts/{postId}/comments/{commentId}",post.getId(),commentId).with(user(user))
                .content(mapper.writeValueAsString(updateParam))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.content").value("update"));
    }

    @DisplayName("댓글_삭제_시_성공")
    @Test
    void 댓글_삭제() throws Exception {

        // given
        Long commentId = 11L;

        // when-then
        mockMvc.perform(
                delete("/api/posts/{postId}/comments/{commentId}",post.getId(),commentId)
                .with(user(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("deleted"));
    }

}
