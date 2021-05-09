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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.royalblood.happy6house.domain.Role.ROLE_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        principal.setId(userId);

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
}
