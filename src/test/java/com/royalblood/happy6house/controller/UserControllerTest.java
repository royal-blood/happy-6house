package com.royalblood.happy6house.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.param.UserCreateParam;
import com.royalblood.happy6house.param.UserUpdateParam;
import com.royalblood.happy6house.security.jwt.JwtAuthenticationEntryPoint;
import com.royalblood.happy6house.security.jwt.JwtTokenUtil;
import com.royalblood.happy6house.service.JwtUserDetailsService;
import com.royalblood.happy6house.service.dto.UserCreateDto;
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
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    JwtUserDetailsService jwtUserDetailsService;
    @MockBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    JwtTokenUtil jwtTokenUtil;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("회원가입에서 성공 시 성공 메시지를 리턴하는지 확인")
    @Test
    void 회원가입_성공_메시지_리턴() throws Exception {
        // given
        UserCreateParam userCreate = UserCreateParam.builder()
                .email("hi@hi.com").password("hi")
                .name("create").picture(null)
                .build();
        Long userId = 10L;

        when(jwtUserDetailsService.join(any(UserCreateDto.class))).thenReturn(userId);


        // when - then
        mockMvc.perform(post("/api/users")
                    .content(mapper.writeValueAsString(userCreate))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("OK"));
    }


    @DisplayName("갱신 url의 id(갱신 대상)와 요청한 회원의 id가 다르면 예외 발생하는지 확인")
    @Test
    void 갱신_url_id와_회원_id가_다르면_예외_발생() throws Exception {
        // given
        UserUpdateParam userUpdate = UserUpdateParam.builder()
                .name("bye").picture("/z1qlQ1ds")
                .build();

        User principal = User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").picture(null)
                .auth(ROLE_USER.getText())
                .build();
        when(principal.getId()).thenReturn(20L);

        Long queryStrUserId = 10L;

        // when - then
        mockMvc.perform(put("/api/users/{id}", queryStrUserId)
                .with(user(principal))
                .content(mapper.writeValueAsString(userUpdate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("unauthorized"));
    }

    @DisplayName("회원 탈퇴 url의 id(탈퇴 대상)와 요청한 회원의 id가 다르면 예외 발생하는지 확인")
    @Test
    void 탈퇴_url_id와_회원_id가_다르면_예외_발생() throws Exception {
        // given
        User principal = User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").picture(null)
                .auth(ROLE_USER.getText())
                .build();
        when(principal.getId()).thenReturn(20L);

        Long queryStrUserId = 10L;

        // when - then
        mockMvc.perform(delete("/api/users/{id}", queryStrUserId)
                .with(user(principal)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("unauthorized"));
    }
}