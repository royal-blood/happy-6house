package com.royalblood.happy6house.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.royalblood.happy6house.utils.EnumType;

public enum PostCategory implements EnumType {
    ETC,
    GENERAL,
    GUESTBOOK,
    WEEKLYNEWS;


    @JsonCreator
    public static PostCategory from(String s) {
        return PostCategory.valueOf(s.toUpperCase());
    }

    @Override
    public String getId() {
        return this.name();
    }

    @Override
    public String getText() {
        return this.name();
    }
}
