package com.royalblood.happy6house.service;

import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.repository.JdbcUserRepository;
import com.royalblood.happy6house.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        User user2 = new User();
        user2.setName("spring");
        user2.setNickname("hi");

        // when
        userService.join(user1);
        // 예외가 발생해야 한다
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.join(user2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}