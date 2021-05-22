package com.royalblood.happy6house.repository.post;

import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.PostCategory;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static com.royalblood.happy6house.domain.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpringDataJpaPostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @DisplayName("게시글을 저장하고 저장된 게시글의 id가 nulL이 아니면 성공")
    @Test
    public void 게시글_저장() {
        final User user = User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").auth(ROLE_USER.getText())
                .build();
        ReflectionTestUtils.setField(user, "id", 10L);

        final Post post = Post.builder()
                .title("This is title")
                .content("This is content")
                .category(PostCategory.GENERAL)
                .user(user)
                .build();

        final Post savePost = postRepository.save(post);

        assertThat(savePost.getId()).isNotNull();
    }
}