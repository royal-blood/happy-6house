package com.royalblood.happy6house.service;

import com.royalblood.happy6house.domain.Role;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserServiceImplIntegrationTest {
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    void 회원가입() {
        // given
        User user = new User();
        user.setName("hello");
        user.setNickname("hi");
        user.setEmail("hi@hi.com");
        user.setRole(Role.USER);

        // when
        Long saveId = userService.join(user);

        // then
        User findUser = userService.findOne(saveId).get();
        assertThat(user.getName()).isEqualTo(findUser.getName());
    }

    @Test
    void 중복_회원_예외() {
        // given
        User user1 = new User();
        user1.setName("spring");
        user1.setNickname("hi");
        user1.setEmail("hi@hi.com");
        user1.setRole(Role.USER);

        User user2 = new User();
        user2.setName("spring");
        user2.setNickname("hi");
        user2.setEmail("hi@hi.com");
        user2.setRole(Role.USER);

        // when
        userService.join(user1);
        // 예외가 발생해야 한다
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.join(user2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}