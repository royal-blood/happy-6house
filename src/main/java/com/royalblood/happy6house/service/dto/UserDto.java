package com.royalblood.happy6house.service.dto;

import com.royalblood.happy6house.domain.User;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String auth;
    private String picture;

    @Builder
    private UserDto(Long id, String email, String auth, String picture, String name) {
        this.id = id;
        this.email = email;
        this.auth = auth;
        this.picture = picture;
        this.name = name;
    }

    public static UserDto of(User user) {
        return builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .auth(user.getAuth())
                .picture(user.getPicture())
                .build();
    }
}