package com.royalblood.happy6house.domain;

import com.royalblood.happy6house.utils.EnumType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum  Role implements EnumType {
    ROLE_USER,
    ROLE_MODIFIER,
    ROLE_ADMIN;

    @Override
    public String getId() {
        return this.name();
    }

    @Override
    public String getText() {
        return this.name();
    }
}
