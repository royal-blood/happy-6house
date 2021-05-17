package com.royalblood.happy6house.controller;

import com.royalblood.happy6house.domain.PostCategory;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.param.PostCreateParam;
import com.royalblood.happy6house.param.PostUpdateParam;
import com.royalblood.happy6house.service.PostService;
import com.royalblood.happy6house.service.dto.PostCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/posts")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class PostController {

    @Autowired
    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> post(@RequestBody PostCreateParam create,
                                  @AuthenticationPrincipal User user) {
        PostCreateDto createDto = create.toDto();
        createDto.setUser(user);

        return ResponseEntity.ok(postService.post(createDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @GetMapping
    public ResponseEntity<?> findByCategory(@RequestParam PostCategory category,
                                            @RequestParam long offset,
                                            @RequestParam int limit) {
        return ResponseEntity.ok(postService.findByCategory(category, offset, limit));
    }


//    @DeleteMapping("/posts/{id}")
//    public ResponseEntity<?> delete(@PathVariable("id") Long id,
//                                    @AuthenticationPrincipal User user) {
//    }
}
