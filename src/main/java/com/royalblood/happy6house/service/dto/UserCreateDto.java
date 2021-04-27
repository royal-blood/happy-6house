package com.royalblood.happy6house.service.dto;

import com.royalblood.happy6house.domain.User;
import lombok.*;

import java.time.LocalDate;

//@Getter
//@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCreateDto {
    private String email;
    private String name;
    private String password;
    private String auth;
    private String picture;

    @Builder
    private UserCreateDto(String email, String password, String auth, String picture, String name) {
        this.email = email;
        this.password = password;
        this.auth = auth;
        this.picture = picture;
        this.name = name;
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .name(name)
                .password(password)
                .picture(picture)
                .auth(auth)
                .build();
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}