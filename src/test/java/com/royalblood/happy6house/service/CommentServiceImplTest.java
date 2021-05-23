package com.royalblood.happy6house.service;

import com.royalblood.happy6house.domain.Comment;
import com.royalblood.happy6house.domain.Post;
import com.royalblood.happy6house.domain.PostCategory;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.repository.comment.CommentRepository;
import com.royalblood.happy6house.service.dto.CommentCreateDto;
import com.royalblood.happy6house.service.dto.CommentDto;
import com.royalblood.happy6house.service.dto.CommentUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.royalblood.happy6house.domain.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    private User user;
    private Post post;

    @BeforeEach
    void init() {
        user = User.builder()
                .email("hi@hi.com").name("hello")
                .password("hi").auth(ROLE_USER.getText())
                .build();
        ReflectionTestUtils.setField(user, "id", 10L);

        post = Post.builder()
                .title("turtle title")
                .content("test post")
                .category(PostCategory.GENERAL)
                .user(user)
                .build();
        post.setId(1L);
    }

    @DisplayName("댓글 작성 후 생성 댓글의 id 반환 하면 성공")
    @Test
    void 댓글_생성_및_id_반환() {

        // given
        CommentCreateDto createDto = CommentCreateDto.builder()
                .content("comment content")
                .post(post)
                .user(user)
                .build();
        Comment comment = createDto.toEntity();
        comment.setId(1L);

        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        Long id = commentService.create(createDto);

        // then
        then(commentRepository)
                .should(times(1))
                .save(any(Comment.class));
        assertThat(id).isSameAs(comment.getId());
    }

    @DisplayName("주어진 유저가 작성한 댓글 전부 조회할 때 성공")
    @Test
    void 유저로_댓글_조회() {

        // given
        int totalCommentSize = 10;

        List<Comment> comments = new ArrayList<>();

        for (int i=0; i<totalCommentSize; i++) {
            CommentCreateDto createDto = CommentCreateDto.builder()
                    .content("comment content")
                    .post(post)
                    .user(user)
                    .build();
            Comment comment = createDto.toEntity();

            comments.add(comment);
        }

        given(commentRepository
                .findByUserId(any(Long.class))).willReturn(comments);

        // when
        List<CommentDto> list = commentService.findByUserId(user.getId());

        // then
        then(commentRepository)
                .should(times(1))
                .findByUserId(any(Long.class));
        assertThat(list.size()).isSameAs(totalCommentSize);
    }

    @DisplayName("주어진 게시글에 작성된 댓글 전부 조회할 때 성공")
    @Test
    void 게시글로_댓글_조회() {

        // given
        int totalCommentSize = 10;

        List<Comment> comments = new ArrayList<>();

        for (int i=0; i<totalCommentSize; i++) {
            CommentCreateDto createDto = CommentCreateDto.builder()
                    .content("comment content")
                    .post(post)
                    .user(user)
                    .build();
            Comment comment = createDto.toEntity();

            comments.add(comment);
        }

        given(commentRepository
                .findByPostId(any(Long.class))).willReturn(comments);

        // when
        List<CommentDto> list = commentService.findByPostId(post.getId());

        // then
        then(commentRepository)
                .should(times(1))
                .findByPostId(any(Long.class));
        assertThat(list.size()).isSameAs(totalCommentSize);
    }

    @DisplayName("id로 댓글 조회할 때 성공")
    @Test
    void id로_댓글_조회() {

        // given
        final Comment comment = Comment.builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        Long commentId = 11L;
        ReflectionTestUtils.setField(comment,"id", commentId);

        given(commentRepository
                .findById(any(Long.class))).willReturn(Optional.of(comment));

        // when
        CommentDto commentDto = commentService.findById(commentId);

        // then
        then(commentRepository)
                .should(times(1))
                .findById(any(Long.class));
        assertThat(commentDto.getId()).isSameAs(commentId);

    }

    @DisplayName("부모 댓글 id로 조회할 때 성공")
    @Test
    void 부모_id로_댓글_조회() {

        // given
        final Comment parent = Comment.builder()
                .content("parent content")
                .user(user)
                .post(post)
                .build();

        Long parentId = 1L;
        ReflectionTestUtils.setField(parent,"id", parentId);

        int totalCommentSize = 5;

        List<Comment> children = new ArrayList<>();

        for (int i=0; i<totalCommentSize; i++) {
            CommentCreateDto createDto = CommentCreateDto.builder()
                    .content("comment content")
                    .post(post)
                    .user(user)
                    .parent(parent)
                    .build();
            Comment comment = createDto.toEntity();

            children.add(comment);
        }

        given(commentRepository
                .findByParentId(any(Long.class))).willReturn(children);

        // when
        List<CommentDto> list = commentService.findByParentId(parentId);

        // then
        then(commentRepository)
                .should(times(1))
                .findByParentId(any(Long.class));
        assertThat(list.size()).isSameAs(totalCommentSize);
    }

    @DisplayName("댓글 수정할 때 성공")
    @Test
    void 댓글_수정() {

        // given
        final Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content("old content")
                .build();
        Long commentId = 11L;
        ReflectionTestUtils.setField(comment, "id", commentId);

        String newContent = "new content";
        CommentUpdateDto updateDto = CommentUpdateDto.builder()
                .content(newContent)
                .build();

        given(commentRepository
                .findById(any(Long.class))).willReturn(Optional.of(comment));

        // when
        CommentDto updated = commentService.update(user.getId(),post.getId(),commentId,updateDto);

        // then
        then(commentRepository)
                .should(times(1))
                .findById(any(Long.class));
        assertThat(updated.getContent()).isSameAs(newContent);

    }

    @DisplayName("댓글 삭제 쿼리 실행되면 성공")
    @Test
    void 댓글_삭제() {

        // given

        final Comment comment = Comment.builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();
        Long commentId = 11L;
        ReflectionTestUtils.setField(comment,"id",commentId);

        given(commentRepository
                .findById(any(Long.class))).willReturn(Optional.of(comment));

        // when
        commentService.delete(user.getId(),post.getId(),commentId);

        // then
        then(commentRepository)
                .should(times(1))
                .findById(any(Long.class));
        then(commentRepository)
                .should(times(1))
                .delete(any(Long.class));
    }
}
