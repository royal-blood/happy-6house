package com.royalblood.happy6house.param;

import com.royalblood.happy6house.service.dto.UserCreateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class UserCreateParam {
    String email;
    String password;
    String name;
    String picture;

    @Builder
    UserCreateParam(String email, String password, String name, String picture) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.picture = picture;
    }

    public UserCreateDto toDto() {
        return UserCreateDto.builder()
                .email(email)
                .password(password)
                .name(name)
                .picture(picture)
                .build();
    }
}
