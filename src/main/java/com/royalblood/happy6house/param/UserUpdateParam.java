package com.royalblood.happy6house.param;

import com.royalblood.happy6house.service.dto.UserUpdateDto;
import lombok.Getter;

@Getter
public class UserUpdateParam {
    private String name;
    private String picture;

    public UserUpdateDto toDto() {
        return UserUpdateDto.builder()
                .name(name)
                .picture(picture)
                .build();
    }

}
