package com.royalblood.happy6house.config.auth.dto;

import com.royalblood.happy6house.domain.Role;
import com.royalblood.happy6house.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
 */
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        // OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 value 하나하나를 변환해야 한다
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /**
     * OAuthAttributes에서 엔티티를 생성하는 시점은 처음 가입할 때 호출
     */
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST) // 가입 시 기본 권한은 GUEST
                .build();
    }
}