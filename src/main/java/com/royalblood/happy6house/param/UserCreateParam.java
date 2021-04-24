package com.royalblood.happy6house.param;

import com.royalblood.happy6house.service.dto.UserCreateDto;
import lombok.Getter;


@Getter
public class UserCreateParam {
    String email;
    String password;
    String name;
    String picture;

    public UserCreateDto toDto() {
        return UserCreateDto.builder()
                .email(email)
                .password(password)
                .name(name)
                .picture(picture)
                .build();
    }
}
