package com.royalblood.happy6house.service;

import com.royalblood.happy6house.exception.NotFoundException;
import com.royalblood.happy6house.service.dto.UserCreateDto;
import com.royalblood.happy6house.service.dto.UserDto;
import com.royalblood.happy6house.service.dto.UserUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.royalblood.happy6house.domain.Role.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class JwtUserDetailsServiceIntegrationTest {
    @Autowired JwtUserDetailsService jwtUserDetailsService;

    @Test
    void 회원가입() {
        // given
        UserCreateDto createDto = UserCreateDto.builder()
                .email("hi@hi.com")
                .password("1234")
                .name("spring")
                .picture("1")
                .build();

        // when
        Long saveId = jwtUserDetailsService.join(createDto);

        // then
        UserDto findUser = jwtUserDetailsService.findById(saveId);
        assertThat(createDto.getEmail()).isEqualTo(findUser.getEmail());
        assertThat(findUser.getAuth()).isEqualTo(ROLE_USER.getText());
    }

    @Test
    void 중복_회원_예외() {
        // given
        UserCreateDto createDto1 = UserCreateDto.builder()
                .email("hi@hi.com")
                .password("1234")
                .name("spring")
                .picture("1")
                .build();

        UserCreateDto createDto2 = UserCreateDto.builder()
                .email("hi@hi.com")
                .password("3456")
                .name("summer")
                .picture("2")
                .build();


        // when
        jwtUserDetailsService.join(createDto1);
        // 예외가 발생해야 한다
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jwtUserDetailsService.join(createDto2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

    @Test
    void 회원_정보_갱신() {
        // given
        UserCreateDto createDto = UserCreateDto.builder()
                .email("hi@hi.com")
                .password("1234")
                .name("spring")
                .picture("1")
                .build();

        String updatedName = "summer";
        String updatedpicture = "2";
        UserUpdateDto updateDto = UserUpdateDto.builder()
                .name(updatedName)
                .picture(updatedpicture)
                .build();

        Long saveId = jwtUserDetailsService.join(createDto);

        // when
        jwtUserDetailsService.update(saveId, updateDto);

        // then
        UserDto findUser = jwtUserDetailsService.findById(saveId);
        assertThat(createDto.getEmail()).isEqualTo(findUser.getEmail());
        assertThat(updatedName).isEqualTo(findUser.getName());
        assertThat(updatedpicture).isEqualTo(findUser.getPicture());
    }

    @Test
    void 회원_탈퇴() {
        // given
        UserCreateDto createDto = UserCreateDto.builder()
                .email("hi@hi.com")
                .password("1234")
                .name("spring")
                .picture("1")
                .build();

        Long saveId = jwtUserDetailsService.join(createDto);

        // when
        jwtUserDetailsService.deleteById(saveId);

        // then
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> jwtUserDetailsService.findById(saveId));
    }
}