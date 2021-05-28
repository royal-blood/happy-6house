package com.royalblood.happy6house.repository.comment;

import com.royalblood.happy6house.domain.Comment;
import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.PostCategory;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.repository.post.PostRepository;
import com.royalblood.happy6house.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.royalblood.happy6house.domain.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.ANY;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = ANY)
public class SpringDataJpaCommentRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User user;
    private Post post;

    @BeforeEach
    public void init() {
        user = userRepository.save(User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").auth(ROLE_USER.getText())
                .build());

        post = postRepository.save(Post.builder()
                .title("turtle title")
                .content("test post")
                .category(PostCategory.GENERAL)
                .user(user)
                .build());
    }

    @DisplayName("댓글 작성 후 댓글 id가 null이 아니면 성공")
    @Test
    public void 댓글_작성() {

        // given
        final Comment comment = Comment.builder()
                .content("command comment")
                .user(user)
                .post(post)
                .build();

        // when
        final Comment saved = commentRepository.save(comment);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @DisplayName("댓글 삭제 후 조회 못하면 성공")
    @Test
    public void 댓글_삭제() {

        // given
        final Comment saved = commentRepository.save(
                Comment.builder()
                        .content("command comment")
                        .user(user)
                        .post(post)
                        .build());

        // when
        commentRepository.delete(saved.getId());

        final Optional<Comment> deleted =
                commentRepository.findById(saved.getId());

        // then
        assertThat(deleted.isEmpty()).isTrue();
    }

}
