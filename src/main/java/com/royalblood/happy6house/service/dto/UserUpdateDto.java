package com.royalblood.happy6house.service.dto;

import com.royalblood.happy6house.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUpdateDto {
    private String name;
    private String picture;

    @Builder
    private UserUpdateDto(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public void apply(User user) {
        user.update(name, picture);
    }
}