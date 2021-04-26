package com.royalblood.happy6house.param;

import com.royalblood.happy6house.service.dto.UserUpdateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateParam {
    private String name;
    private String picture;

    @Builder
    UserUpdateParam(String name, String picture) {
        this.name = name;
        this.picture = picture;
    }

    public UserUpdateDto toDto() {
        return UserUpdateDto.builder()
                .name(name)
                .picture(picture)
                .build();
    }

}
