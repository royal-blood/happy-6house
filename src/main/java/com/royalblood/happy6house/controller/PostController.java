package com.royalblood.happy6house.controller;

import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.param.PostCreateParam;
import com.royalblood.happy6house.param.UserCreateParam;
import com.royalblood.happy6house.param.UserUpdateParam;
import com.royalblood.happy6house.response.MessageResponse;
import com.royalblood.happy6house.service.PostService;
import com.royalblood.happy6house.service.dto.PostCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class PostController {

    @Autowired private PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<?> post(@RequestBody PostCreateParam create,
                                  @AuthenticationPrincipal User user) {
        PostCreateDto createDto = create.toDto();
        createDto.setUser(user);

        return ResponseEntity.ok(postService.post(createDto));
    }

//    @GetMapping("/posts/{id}")
//    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
//    }

//    @PutMapping("/posts/{id}")
//    public ResponseEntity<?> update(@PathVariable("id") Long id,
//                                    @RequestBody PostUpdateParam update,
//                                    @AuthenticationPrincipal User user) {
//    }

//    @DeleteMapping("/posts/{id}")
//    public ResponseEntity<?> delete(@PathVariable("id") Long id,
//                                    @AuthenticationPrincipal User user) {
//    }
}
