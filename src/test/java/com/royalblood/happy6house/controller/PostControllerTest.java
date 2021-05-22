package com.royalblood.happy6house.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.royalblood.happy6house.domain.PostCategory;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.param.PostCreateParam;
import com.royalblood.happy6house.security.jwt.JwtAuthenticationEntryPoint;
import com.royalblood.happy6house.security.jwt.JwtTokenUtil;
import com.royalblood.happy6house.service.JwtUserDetailsService;
import com.royalblood.happy6house.service.PostService;
import com.royalblood.happy6house.service.dto.PostCreateDto;
import com.royalblood.happy6house.service.dto.PostDto;
import com.royalblood.happy6house.service.dto.UserDto;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.royalblood.happy6house.domain.Role.ROLE_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PostController.class)
class PostControllerTest {
    // for SecurityConfig
    @MockBean JwtUserDetailsService jwtUserDetailsService;
    @MockBean JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean JwtTokenUtil jwtTokenUtil;

    @MockBean
    PostService postService;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("글 작성 성공 시 성공 메시지를 리턴하는지 확인")
    @Test
    void 글작성_성공_메시지_리턴() throws Exception {
        // given
        Long userId = 10L;
        User principal = User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").picture(null)
                .auth(ROLE_USER.getText())
                .build();
        ReflectionTestUtils.setField(principal, "id", userId);

        Long postId = 100L;
        PostCreateParam createParam = PostCreateParam.builder()
                .title("this is title")
                .content("this is content")
                .category(PostCategory.GENERAL)
                .build();

        when(postService.post(any(PostCreateDto.class))).thenReturn(postId);


        // when - then
        mockMvc.perform(post("/api/posts")
                .with(user(principal))
                .content(mapper.writeValueAsString(createParam))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(postId));
    }

    @DisplayName("id로 게시글 조회하여 게시글이 리턴되면 성공")
    @Test
    void id로_게시글_조회() throws Exception {
        // given
        Long principalId = 20L;
        User principal = User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").picture(null)
                .auth(ROLE_USER.getText())
                .build();
        ReflectionTestUtils.setField(principal, "id", principalId);

        Long userId = 10L;
        UserDto userDto = UserDto.builder()
                .email("hello@hello.com").name("good")
                .picture(null).auth(ROLE_USER.getText())
                .build();
        ReflectionTestUtils.setField(userDto, "id", userId);

        Long postId = 100L;
        PostDto postDto = PostDto.builder()
                .title("this is title")
                .content("this is content")
                .category(PostCategory.GENERAL)
                .userDto(userDto)
                .build();
        ReflectionTestUtils.setField(postDto, "createdDate", LocalDateTime.now());
        ReflectionTestUtils.setField(postDto, "modifiedDate", LocalDateTime.now());

        given(postService.findById(postId)).willReturn(postDto);


        // when
        mockMvc.perform(get("/api/posts/{postId}", postId)
                .with(user(principal))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postDto.getId()))
                .andExpect(jsonPath("$.user.id").value(postDto.getUserDto().getId()));

        // then
        verify(postService).findById(any(Long.class));
    }

    @DisplayName("category로 게시글 조회하여 게시글 리스트가 리턴되면 성공")
    @Test
    void category로_게시글_조회() throws Exception {
        // given
        Long principalId = 20L;
        User principal = User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").picture(null)
                .auth(ROLE_USER.getText())
                .build();
        ReflectionTestUtils.setField(principal, "id", principalId);

        Long userId = 10L;
        UserDto userDto = UserDto.builder()
                .email("hello@hello.com").name("good")
                .picture(null).auth(ROLE_USER.getText())
                .build();
        ReflectionTestUtils.setField(userDto, "id", userId);

        int totalGeneralPostsSize = 10;
        List<PostDto> postDtoList = new ArrayList<>();
        for (long i = 1; i <= totalGeneralPostsSize; i++) {
            PostDto postDto = PostDto.builder()
                    .title("title")
                    .content("content")
                    .category(PostCategory.GENERAL)
                    .userDto(userDto)
                    .build();
            ReflectionTestUtils.setField(postDto, "id", i);
            ReflectionTestUtils.setField(postDto, "createdDate", LocalDateTime.now());
            ReflectionTestUtils.setField(postDto, "modifiedDate", LocalDateTime.now());
            postDtoList.add(postDto);
        }

        long offset = 0;
        int limit = 10;
        given(postService.findByCategory(PostCategory.GENERAL, offset, limit)).willReturn(postDtoList);


        // when
        mockMvc.perform(get("/api/posts")
                .with(user(principal))
                .queryParam("category", PostCategory.GENERAL.name().toLowerCase())
                .queryParam("offset", Long.toString(offset))
                .queryParam("limit", Integer.toString(limit))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(totalGeneralPostsSize));

        // then
        verify(postService).findByCategory(any(PostCategory.class), anyLong(), anyInt());
    }
}
