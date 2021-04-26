package com.royalblood.happy6house.service;


import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.exception.NotFoundException;
import com.royalblood.happy6house.repository.user.UserRepository;
import com.royalblood.happy6house.service.dto.UserCreateDto;
import com.royalblood.happy6house.service.dto.UserDto;
import com.royalblood.happy6house.service.dto.UserUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.royalblood.happy6house.domain.Role.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtUserDetailsServiceTest {
    @InjectMocks
    JwtUserDetailsService jwtUserDetailsService;
    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;

    @Test
    void 회원가입을_하면_생성된_유저의_id를_반환한다() {
        // given
        UserCreateDto createDto = UserCreateDto.builder()
                .email("hi@hi.com").password("hi")
                .name("hello").auth(ROLE_USER.getText())
                .picture(null)
                .build();
        User createdUser = createDto.toEntity();
        createdUser.setId(10L);

        when(userRepository.save(any(User.class))).thenReturn(createdUser);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encrypted");


        // when
        Long saveId = jwtUserDetailsService.join(createDto);

        // then
        assertThat(saveId).isSameAs(createdUser.getId());
    }

    @Test
    void 유저_목록_조회를_하면_유저DTO_리스트를_반환한다() {
        // given
        User user1 = User.builder()
                .email("hi@hi.com").name("hello").password("pass")
                .auth(ROLE_USER.getText()).picture(null)
                .build();

        User user2 = User.builder()
                .email("bye@bye.com").name("bye").password("pswd")
                .auth(ROLE_USER.getText()).picture("1")
                .build();

        List<User> userList = new ArrayList<>();
        userList.add(user1); userList.add(user2);

        List<String> expectedEmails = userList.stream()
                .map(e->e.getEmail())
                .collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(userList);

        // when
        List<UserDto> userDtoList = jwtUserDetailsService.findUsers();

        // then
        List<String> actureEmails = userDtoList.stream()
                .map(e->e.getEmail())
                .collect(Collectors.toList());

        assertThat(actureEmails.containsAll(expectedEmails)).isTrue();
    }

    @Test
    void 유저_조회를_하면_유저DTO를_반환한다() {
        // given
        Long userId = 10L;
        User user = User.builder()
                .email("hi@hi.com").name("hello").password("pass")
                .auth(ROLE_USER.getText()).picture(null)
                .build();

        Optional<User> userOptional = Optional.of(user);
        when(userRepository.findById(userId)).thenReturn(userOptional);

        // when
        UserDto actualUserDto = jwtUserDetailsService.findById(userId);

        // then
        assertThat(actualUserDto.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void 유저_정보_갱신_성공() {
        // given
        Long userId = 10L;
        User user = User.builder()
                .email("hi@hi.com").name("hello").password("pass")
                .auth(ROLE_USER.getText()).picture(null)
                .build();

        String updatedName = "bye";
        String updatedPicture = "path";
        UserUpdateDto updateDto = UserUpdateDto.builder()
                .name(updatedName).picture(updatedPicture)
                .build();

        Optional<User> userOptional = Optional.of(user);
        when(userRepository.findById(userId)).thenReturn(userOptional);

        // when
        UserDto actualUserDto = jwtUserDetailsService.update(userId, updateDto);

        // then
        assertThat(actualUserDto.getName()).isEqualTo(updatedName);
        assertThat(actualUserDto.getPicture()).isEqualTo(updatedPicture);
    }

    @Test
    void 유저_탈퇴_성공() {
        // given
        Long userId = 10L;
        User user = User.builder()
                .email("hi@hi.com").name("hello").password("pass")
                .auth(ROLE_USER.getText()).picture(null)
                .build();

        Optional<User> userOptional = Optional.of(user);
        when(userRepository.findById(any(Long.class))).thenReturn(userOptional);
        doNothing().when(userRepository).delete(any(Long.class));

        // when
        jwtUserDetailsService.delete(userId);
    }

    @Test
    void 유저_탈퇴시_해당_id의_유저가_없어_예외_발생() {
        // given
        Long userId = 10L;
        Optional<User> userEmptyOptional = Optional.empty();
        when(userRepository.findById(any(Long.class))).thenReturn(userEmptyOptional);

        // when - then
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> jwtUserDetailsService.delete(userId));
    }
}
