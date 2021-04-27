package com.royalblood.happy6house.repository.user;

import com.royalblood.happy6house.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.royalblood.happy6house.domain.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest // 내부적으로 @transactional 선언
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpringDataJpaUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 유저_저장() {
        final User user = User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").auth(ROLE_USER.getText())
                .build();
        final User saveUser = userRepository.save(user);
        assertThat(saveUser.getId()).isNotNull();
    }
}