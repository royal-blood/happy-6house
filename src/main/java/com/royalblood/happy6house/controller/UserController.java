package com.royalblood.happy6house.controller;

import com.royalblood.happy6house.param.UserCreateParam;
import com.royalblood.happy6house.param.UserUpdateParam;
import com.royalblood.happy6house.domain.User;
import com.royalblood.happy6house.response.MessageResponse;
import com.royalblood.happy6house.service.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final JwtUserDetailsService jwtUserDetailsService;

    @PostMapping("/users")
    public ResponseEntity<?> signUp(@RequestBody UserCreateParam create) {
        try {
            Long id = jwtUserDetailsService.join(create.toDto());
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(new MessageResponse(e.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("OK"));
    }

    @GetMapping("/users")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(jwtUserDetailsService.findUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(jwtUserDetailsService.findById(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                          @RequestBody UserUpdateParam update,
                                          @AuthenticationPrincipal User user) {
        if (!user.getId().equals(id))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponse("unauthorized"));

        return ResponseEntity.ok(jwtUserDetailsService.update(id, update.toDto()));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id,
                                         @AuthenticationPrincipal User user) {
        if (!user.getId().equals(id))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("unauthorized"));

        jwtUserDetailsService.delete(id);
        return ResponseEntity.ok(new MessageResponse("deleted"));
    }
}
