package com.royalblood.happy6house.config.auth.dto;

import com.royalblood.happy6house.domain.User;
import lombok.Getter;

import java.io.Serializable;

/**
 * 세션에 사용자 정보를 저장하기 위한 직렬화 기능을 가진 Dto
 * - @Entty User class를 이용하지 않는 이유
 *  - @OneToMany, @ManyToMany 등 자식 엔티티를 갖는다면 직렬화 대상에 자식들까지 포함되어
 *    성능 이슈, 부수 효과가 발생할 확률이 높다
 */
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String nickname;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.nickname = user.getNickname() ;
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
