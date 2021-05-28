package com.royalblood.happy6house.controller;

import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.param.CommentCreateParam;
import com.royalblood.happy6house.param.CommentUpdateParam;
import com.royalblood.happy6house.service.CommentService;
import com.royalblood.happy6house.service.dto.CommentCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api")
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @GetMapping("/users/{id}/comments")
    public ResponseEntity<?> findByUserId(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(commentService.findByUserId(userId));
    }

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<?> findByPostId(@PathVariable("id") Long postId, @RequestParam(required = false) Long parentId) {

        return ResponseEntity
                .ok(Optional.ofNullable(parentId).isPresent()?
                        commentService.findByParentId(parentId):
                        commentService.findByPostId(postId));
    }

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<?> createByPostId(
            @PathVariable("id") Long postId,
            @RequestBody CommentCreateParam create,
            @AuthenticationPrincipal User user) {

        CommentCreateDto createDto = create.toDto();

        createDto.setUser(user);
        createDto.setPost(postId);

        Optional.ofNullable(create.getParentId())
                .ifPresent(createDto::setParent);

        return ResponseEntity.ok(commentService.create(createDto));
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> update(@PathVariable Long postId,
                                    @PathVariable Long commentId,
                                    @RequestBody CommentUpdateParam update,
                                    @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(commentService.update(user.getId(), postId, commentId, update.toDto()));
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> delete(@PathVariable Long postId,
                                    @PathVariable Long commentId,
                                    @AuthenticationPrincipal User user) {

        commentService.delete(user.getId(), postId, commentId);

        return ResponseEntity.ok("deleted");
    }
}
